package game

import play.api.libs.json.{Json, JsResult, JsValue}
import unit.{AtomicAction, Unit}
import world.World
import utils.Algorithms

trait Order {
  def decompose(world: World, unit: game.unit.Unit): List[AtomicAction]
  def reads(json: JsValue): JsResult[Order] = (json \ "name").as[String] match {
    case "move" => Json.reads[Move].reads(json)
  }
}
/* x, y - top left corner of the building */
//case class Build(x: Int, y: Int, buildingType: Building) extends Order
case class Move(x: Int, y: Int) extends Order {
  def decompose(world: World, unit: Unit): List[AtomicAction] = {
    List[AtomicAction]((Algorithms.astar((unit.x, unit.y), (x, y), world.getUnitsPassability(unit.player, unit.ch.kind)) map { p =>
      // todo change
      new game.unit.Move(p._1, p._2, unit, unit.ch.ticksToMove, unit.ch.ticksToMove)
    }):_*)
  }
}

object Order {
//  implicit val actionReads = Json.reads[Order]
}