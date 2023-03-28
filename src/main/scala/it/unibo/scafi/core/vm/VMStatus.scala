package it.unibo.scafi.core.vm

import it.unibo.scafi.core.{Path, Slot}

// VMStatus is an immutable object. Each update creates a new VMStatus.
trait VMStatus {
  // The current path
  val path: Path

  // The number of nodes at the same level in the tree.
  // in other words, if we have two calls to the same construct we can differentiate between the two using this index.
  /*
            rep
           /   \
         nbr   nbr

         the two calls to nbr() will be identified as nbr(0) and nbr(1)
   */
  val index: Int
  // The ID of the neighbour where the computation currently is.
  val neighbour: Option[Int]

  // Returns true if the field 'neighbourg' is defined, false otherwise
  def isFolding: Boolean
  // update the VMStatus by setting the field 'neighbourg' to the parameter 'id'
  def foldInto(id: Option[Int]): VMStatus
  // Update the VMStatus by setting the field 'neighbourg' to None
  def foldOut(): VMStatus
  // Push the specified Slot on the current path. Reset the index.
  def nest(s: Slot): VMStatus
  // increment the current index
  def incIndex(): VMStatus
  // push the current (path, index, neighbour) on the stack
  def push(): VMStatus
  // pop the first element of the stack
  def pop(): VMStatus
}

object VMStatus {
  def apply(path: Path = Path()): VMStatus = VMStatusImpl(path)

  final private case class VMStatusImpl(
      path: Path,
      index: Int = 0,
      neighbour: Option[Int] = None,
      stack: List[(Path, Int, Option[Int])] = List()
  ) extends VMStatus {

    def isFolding: Boolean = neighbour.isDefined
    def foldInto(id: Option[Int]): VMStatus = VMStatusImpl(path, index, id, stack)
    def foldOut(): VMStatus = VMStatusImpl(path, index, None, stack)
    def push(): VMStatus = VMStatusImpl(path, index, neighbour, (path, index, neighbour) :: stack)
    def pop(): VMStatus = stack match {
      case (p, i, n) :: s => VMStatusImpl(p, i, n, s)
      case _ => throw new Exception()
    }
    def nest(s: Slot): VMStatus = VMStatusImpl(path.push(s), 0, neighbour, stack)
    def incIndex(): VMStatus = VMStatusImpl(path, index + 1, neighbour, stack)
  }
}
