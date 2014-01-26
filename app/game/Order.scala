package game

import play.api.libs.json.{Json, JsResult, JsValue}
import unit.AtomicAction
import world.World
import game.unit
import utils.Algorithms

trait Order {
  def decompose(world: World, unit: game.unit.Unit): Iterator[AtomicAction]
  def reads(json: JsValue): JsResult[Order] = (json \ "name").as[String] match {
    case "move" => Json.reads[Move].reads(json)
  }
}
/* x, y - top left corner of the building */
//case class Build(x: Int, y: Int, buildingType: Building) extends Order
case class Move(x: Int, y: Int) extends Order {
  // todo use A*
  def decompose(world: World, unit: unit.Unit): Iterator[AtomicAction] = {

  }
}

object Order {
//  implicit val actionReads = Json.reads[Order]
}