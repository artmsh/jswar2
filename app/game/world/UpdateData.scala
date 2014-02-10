package game.world

import play.api.libs.json._
import play.api.libs.functional.ContravariantFunctor
import format.pud.Tile
import play.api.libs.json.Json.JsValueWrapper
import game.unit.AtomicAction

case class UpdateData(
        addedUnits: Map[Int, game.unit.Unit], updatedUnits: Map[Int, game.unit.Unit], deletedUnits: List[Int],
        playerStats: PlayerStats, addedTerrain: List[(Int, Int, Tile, Int)], changedTerrain: List[(Int, Int, Tile)]

//      todo fogOfWar  addedVision: List[(Int, Int)], removedVision: List[(Int, Int)]
)

object UpdateData {
  implicit val tileFormat = new Writes[Tile] {
    def writes(o: Tile): JsValue = JsNumber(o.tile)
  }

  implicit val tuple3Format = new Writes[(Int, Int, Tile)] {
    def writes(o: (Int, Int, Tile)): JsValue = JsArray(Array(JsNumber(o._1), JsNumber(o._2), Json.toJson(o._3)))
  }

  implicit val tuple4Format = new Writes[(Int, Int, Tile, Int)] {
    def writes(o: (Int, Int, Tile, Int)): JsValue = JsArray(
      Array(JsNumber(o._1), JsNumber(o._2), Json.toJson(o._3), JsNumber(o._4)))
  }

  implicit val unitFormat = new Writes[game.unit.Unit] {
    def writes(unit: game.unit.Unit): JsValue = {
      var map = Map[String, JsValueWrapper](
        "x" -> unit.x,
        "y" -> unit.y,
        "name" -> unit.name,
        "player" -> unit.player,
        "hp" -> unit.hp,
        "armor" -> unit.armor,
      // todo fix
        "action" -> "still"
      )

      Json.obj(map.toSeq:_*)
    }
  }

  implicit val playerStatsFormat = new Writes[PlayerStats] {
    def writes(ps: PlayerStats): JsValue = Json.obj(
      "gold" -> ps.gold,
      "lumber" -> ps.lumber,
      "oil" -> ps.oil
    )
  }


  implicit def mapIntWrites[V](implicit fmtv: Writes[V]): Writes[Map[Int, V]] =
    OWrites.contravariantfunctorOWrites.contramap[Map[String, V], Map[Int, V]](implicitly[OWrites[Map[String, V]]], m =>
      m map { p => (p._1 + "", p._2) })

  implicit val updateDataFormat = Json.writes[UpdateData]
}