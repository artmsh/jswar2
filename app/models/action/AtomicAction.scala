package models.action

import models.World

trait AtomicAction {
  val next: AtomicAction

  def spentTick(world: World, unit: Unit): AtomicAction
}

class Still extends AtomicAction {
  val next: AtomicAction = this

  def spentTick(world: World, unit: Unit): AtomicAction = this
}

/* precompute A* if not precomputed and if we reach non-empty field - we compute it again */
class Move(val dx: Int, val dy: Int, val unit: Unit, val destinationTime: Int, val next: AtomicAction) extends AtomicAction {
  def spentTick(world: World, unit: Unit): Unit = {
    if (destinationTime > 0) {
      new Move(dx, dy, unit, destinationTime - 1, next)
    } else {
      next
    }
  }
}

class Attack extends AtomicAction {

}