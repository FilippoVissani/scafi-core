package it.unibo.scafi.test.logging

import com.typesafe.scalalogging
import com.typesafe.scalalogging.Logger
import it.unibo.scafi.core.{Context, Export, ExportFactory, Slot}
import it.unibo.scafi.core.vm.{RoundVM, VMStatus}
import it.unibo.scafi.test.CoreTestUtils.ctx


trait RoundVMComponent {
  val env: RoundVMEnvironment
  class RoundVMEnvironment(vm: RoundVM) {
  }
}

trait RoundVMLoggingComponent { this: RoundVMComponent =>
  val logger: RoundVMLogger
  class RoundVMLogger {
    private val logger: scalalogging.Logger = Logger("logback")

    def factory: ExportFactory = {
      logBeforeExecution()
      val out = this.factory
      logAfterExecution()
      out
    }

    def context: Context = {
      logBeforeExecution()
      val out = this.context
      logAfterExecution()
      out
    }

    def exportStack: List[Export] = {
      logBeforeExecution()
      val out = this.exportStack
      logAfterExecution()
      out
    }

    def status: VMStatus = {
      logBeforeExecution()
      val out = this.status
      logAfterExecution()
      out
    }

    def foldedEval[A](expr: => A)(id: Int): Option[A] = {
      logBeforeExecution()
      val out = this.foldedEval(expr)(id)
      logAfterExecution()
      out
    }

    def nest[A](slot: Slot)(write: Boolean, inc: Boolean)(expr: => A): A = {
      logBeforeExecution()
      val out = this.nest(slot)(write, inc)(expr)
      logAfterExecution()
      out
    }

    def locally[A](a: => A): A = {
      logBeforeExecution()
      val out = this.locally(a)
      logAfterExecution()
      out
    }

    def alignedNeighbours(): List[Int] = {
      logBeforeExecution()
      val out = this.alignedNeighbours()
      logAfterExecution()
      out
    }

    def isolate[A](expr: => A): A = {
      logBeforeExecution()
      val out = this.isolate(expr)
      logAfterExecution()
      out
    }

    def newExportStack: Any = {
      logBeforeExecution()
      val out = this.newExportStack
      logAfterExecution()
      out
    }

    def discardExport: Any = {
      logBeforeExecution()
      val out = this.discardExport
      logAfterExecution()
      out
    }

    def mergeExport: Any = {
      logBeforeExecution()
      val out = this.mergeExport
      logAfterExecution()
      out
    }

    private def logBeforeExecution(): Unit = {
      logger.info("BEFORE ------------------------------------")
      logStatus()
    }

    private def logAfterExecution(): Unit = {
      logger.info("AFTER ------------------------------------")
      logStatus()
    }

    private def logStatus(): Unit = {
      logger.info("STATUS => " + this.status)
      logger.info("CONTEXT => " + this.context)
      logger.info("EXPORT STACK => " + this.exportStack)
      logger.info("------------------------------------")
    }
  }
}

object Registry extends RoundVMComponent with RoundVMLoggingComponent {
  override val env: Registry.RoundVMEnvironment = new RoundVMEnvironment(RoundVM(ctx(selfId = 0), ExportFactory()))
  override val logger: Registry.RoundVMLogger = new RoundVMLogger()
}