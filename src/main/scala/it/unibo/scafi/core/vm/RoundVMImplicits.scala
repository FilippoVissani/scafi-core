package it.unibo.scafi.core.vm
import com.typesafe.scalalogging.Logger
import it.unibo.scafi.core.{Context, Export, ExportFactory, Slot}

import scala.language.implicitConversions

object RoundVMImplicits {
  implicit class RoundVMLogging(roundVM: RoundVM) extends RoundVM {

    private val logger: Logger = Logger("logback")

    override def factory: ExportFactory = {
      logStatus()
      val out = roundVM.factory
      logStatus()
      out
    }

    override def context: Context = {
      logStatus()
      val out = roundVM.context
      logStatus()
      out
    }

    override def exportStack: List[Export] = {
      logStatus()
      val out = roundVM.exportStack
      logStatus()
      out
    }

    override def status: VMStatus = {
      logStatus()
      val out = roundVM.status
      logStatus()
      out
    }

    override def foldedEval[A](expr: => A)(id: Int): Option[A] = {
      logStatus()
      val out = roundVM.foldedEval(expr)(id)
      logStatus()
      out
    }

    override def nest[A](slot: Slot)(write: Boolean, inc: Boolean)(expr: => A): A = {
      logStatus()
      val out = roundVM.nest(slot)(write, inc)(expr)
      logStatus()
      out
    }

    override def locally[A](a: => A): A = {
      logStatus()
      val out = roundVM.locally(a)
      logStatus()
      out
    }

    override def alignedNeighbours(): List[Int] = {
      logStatus()
      val out = roundVM.alignedNeighbours()
      logStatus()
      out
    }

    override def isolate[A](expr: => A): A = {
      logStatus()
      val out = roundVM.isolate(expr)
      logStatus()
      out
    }

    override def newExportStack: Any = {
      logStatus()
      val out = roundVM.newExportStack
      logStatus()
      out
    }

    override def discardExport: Any = {
      logStatus()
      val out = roundVM.discardExport
      logStatus()
      out
    }

    override def mergeExport: Any = {
      logStatus()
      val out = roundVM.mergeExport
      logStatus()
      out
    }

    private def logStatus(): Unit = {
      logger.info("------------------------------------")
      logger.info(Thread.currentThread().getStackTrace.last.getMethodName)
      logger.info("STATUS => " + roundVM.status)
      logger.info("CONTEXT => " + roundVM.context)
      logger.info("EXPORT STACK => " + roundVM.exportStack)
      logger.info("------------------------------------")
    }
  }
}
