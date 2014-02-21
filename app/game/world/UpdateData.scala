package game.world

import play.api.libs.json._
import format.pud.Tile
import play.api.libs.json.Json.JsValueWrapper

case class AddedTileInfo(x: Int, y: Int, tile: Tile, vision: Int)
case class UpdatedTileInfo(x:Int, y: Int, tile: Tile)

case class UpdateUnitData(addedUnits: Map[Int, game.unit.Unit], updatedUnits: Map[Int, Map[String, String]],
                          deletedUnits: List[Int]) {
  def isEmpty: Boolean = addedUnits.isEmpty && updatedUnits.isEmpty && deletedUnits.isEmpty
}

case class UpdateData(updateUnitData: UpdateUnitData,
        playerStats: Map[String, String], addedTerrain: List[AddedTileInfo], changedTerrain: List[UpdatedTileInfo]) {

//      todo fogOfWar  addedVision: List[(Int, Int)], removedVision: List[(Int, Int)]
  def isEmpty: Boolean = {
    updateUnitData.isEmpty && playerStats.isEmpty && addedTerrain.isEmpty && changedTerrain.isEmpty
  }
}

object UpdateData {
  implicit val tileFormat = new Writes[Tile] {
    def writes(o: Tile): JsValue = JsNumber(o.tile)
  }

  implicit val unitFormat = new Writes[game.unit.Unit] {
    def writes(unit: game.unit.Unit): JsValue = {
      val map = Map[String, JsValueWrapper](
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

  implicit def mapIntWrites[V](implicit fmtv: Writes[V]): Writes[Map[Int, V]] =
    OWrites.contravariantfunctorOWrites.contramap[Map[String, V], Map[Int, V]](implicitly[OWrites[Map[String, V]]], m =>
      m map { p => (p._1 + "", p._2) })

  implicit val addedTileInfoWrites = Json.writes[AddedTileInfo]
  implicit val updateTileInfoWrites = Json.writes[UpdatedTileInfo]
  implicit val updateDataWrites = Json.writes[UpdateData]
}