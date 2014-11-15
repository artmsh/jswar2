package game

import play.api.libs.json.{Json, JsResult, JsValue}
import game.unit._
import utils.Algorithms
import play.Logger

trait Order {
  def decompose(game: Game, unit: Unit): List[AtomicAction]
  def reads(json: JsValue): JsResult[Order] = (json \ "name").as[String] match {
    case "move" => Json.reads[Move].reads(json)
  }
}

case object Stop extends Order {
  def decompose(game: Game, unit: Unit): List[AtomicAction] = ???
}

/* x, y - top left corner of the building */
//case class Build(x: Int, y: Int, buildingType: Building) extends Order
case class Move(x: Int, y: Int) extends Order {
  def decompose(_game: Game, unit: Unit): List[AtomicAction] = {
    Logger.debug(s"find way for ${unit.id} from x:${unit.x}, y:${unit.y} to x: $x, y: $y")

    val path: List[(Int, Int)] = Algorithms.astar((unit.x, unit.y), (x, y), _game.getUnitsPassability(unit.player, unit.ch.pudUc.basic.kind))

    Logger.debug(path.toString())

    List[AtomicAction]((path map { p =>
      // todo change
      game.unit.Move(p._1, p._2, unit, this, unit.ch.ticksToMove, unit.ch.ticksToMove)
    }):_*) :+ Still(unit)
  }
}

object Order {
//  implicit val actionReads = Json.reads[Order]
}