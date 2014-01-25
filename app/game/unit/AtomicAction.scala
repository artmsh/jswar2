package game.unit

import game.world.World
import play.api.libs.json._
import scala.Unit

trait AtomicAction extends Writes[AtomicAction] {
  def spentTick(world: World, unit: Unit): AtomicAction
}

class Still extends AtomicAction {
  val next: AtomicAction = this

  def spentTick(world: World, unit: Unit): AtomicAction = this

  def reads(json: JsValue): JsResult[AtomicAction] = ???
  def writes(o: AtomicAction): JsValue = Json.obj("name" -> "still")
}

/* precompute A* if not precomputed and if we reach non-empty field - we compute it again */
class Move(val dx: Int, val dy: Int, val unit: Unit, val destinationTime: Int, val next: AtomicAction) extends AtomicAction {
  def spentTick(world: World, unit: Unit): AtomicAction = {
//    if (destinationTime > 0) {
//      new Move(dx, dy, unit, destinationTime - 1, next)
//    } else {
//      next
//    }
    next
  }

  def reads(json: JsValue): JsResult[AtomicAction] = ???
  def writes(o: AtomicAction): JsValue = ???
}

class Attack extends AtomicAction {
  def spentTick(world: World, unit: Unit): AtomicAction = ???

  val next: AtomicAction = this

  def reads(json: JsValue): JsResult[AtomicAction] = ???
  def writes(o: AtomicAction): JsValue = ???
}