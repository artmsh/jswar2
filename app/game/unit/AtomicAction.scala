package game.unit

import game.world.World
import play.api.libs.json._

trait AtomicAction extends Writes[AtomicAction] {
  def spentTick(world: World, unit: Unit)
}

class Still extends AtomicAction {
  val next: AtomicAction = this

  def spentTick(world: World, unit: Unit) {}

  def reads(json: JsValue): JsResult[AtomicAction] = ???
  def writes(o: AtomicAction): JsValue = Json.obj("name" -> "still")
}

/* precompute A* if not precomputed and if we reach non-empty field - we compute it again */
class Move(val dx: Int, val dy: Int, val unit: Unit, val destinationTime: Int) extends AtomicAction {
  def spentTick(world: World, unit: Unit) = {
//    if (destinationTime > 0) {
//      new Move(dx, dy, unit, destinationTime - 1, next)
//    } else {
//      next
//    }
  }

  def reads(json: JsValue): JsResult[AtomicAction] = ???
  def writes(o: AtomicAction): JsValue = ???
}

class Attack extends AtomicAction {
  def spentTick(world: World, unit: Unit) = ???

  def reads(json: JsValue): JsResult[AtomicAction] = ???
  def writes(o: AtomicAction): JsValue = ???
}