package it.unibo.scafi.core

import it.unibo.scafi.core.Slot.{Branch, FoldHood, Nbr, Rep}
import it.unibo.scafi.core.vm.RoundVM

trait ConstructsSemantics extends Language {
  def vm: RoundVM

  override def mid(): Int = vm.self

  /**
   * This method models the time evolution, and it is a construct for dynamically changing fields.
   * Each device evaluates the application of its anonymous function 'fun' repeatedly in asynchronous rounds.
   * At the first round the function is applied to the value 'init', then at each step the function is
   * applied to the value obtained at previous step.
   *
   * For instance, rep(0){(x) => + (x,1))} counts how many rounds each device has computed.
   *
   * @param init the initial value.
   * @param fun the function to compute.
   * @tparam A the type of the computation.
   * @return the result of the computation.
   */
  override def rep[A](init: => A)(fun: A => A): A = {
    vm.nest(Rep(vm.index))(write = vm.unlessFoldingOnOthers) {
      vm.locally {
        fun(vm.previousRoundVal.getOrElse(init))
      }
    }
  }

  /**
   * This method compute the expression on the node and get the result of the same expression for each neighbour.
   * Then proceed to aggregate the results using the given aggregating function.
   *
   * @param init the initial value
   * @param aggr the aggregate function
   * @param expr the expression to execute on the neighbours
   * @tparam A the return type of the computation
   * @return the result of the aggregation
   */
  override def foldhood[A](init: => A)(aggr: (A, A) => A)(expr: => A): A = {
    vm.nest(FoldHood(vm.index))(write = true) { // write export always for performance reason on nesting
      val nbrField = vm
        .alignedNeighbours()
        .map(id => vm.foldedEval(expr)(id).getOrElse(vm.locally(init)))
      vm.isolate(nbrField.fold(vm.locally(init))((x, y) => aggr(x, y)))
    }
  }

  override def branch[A](cond: => Boolean)(thn: => A)(els: => A): A = {
    val tag = vm.locally(cond)
    vm.nest(Branch(vm.index, tag))(write = vm.unlessFoldingOnOthers) {
      vm.neighbour match {
        case Some(nbr) if nbr != vm.self => vm.neighbourVal
        case _ => if (tag) vm.locally(thn) else vm.locally(els)
      }
    }
  }

  /**
   * This method models device-to-neighbour interaction, by returning a field of neighbouring field values.
   * Each device is associated to value returned by expr, which maps any neighbour in the domain to its most recent available value
   *
   * @param expr the expression to get the value of.
   * @tparam A the return type of the expression
   * @return the result of the computation in the neighbour.
   */
  override def nbr[A](expr: => A): A =
    vm.nest(Nbr(vm.index))(write = vm.onlyWhenFoldingOnSelf) {
      vm.neighbour match {
        case Some(nbr) if nbr != vm.self => vm.neighbourVal
        case _ => expr
      }
    }

  def sense[A](name: SensorId): A = vm.localSense(name)

  def nbrvar[A](name: SensorId): A = vm.neighbourSense(name)
}
