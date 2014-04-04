package game.world

import play.api.libs.json._
import format.pud.Tile
import play.api.libs.json.Json.JsValueWrapper

case class AddedTileInfo(x: Int, y: Int, tile: Tile, vision: Int)
case class UpdatedTileInfo(x:Int, y: Int, tile: Tile)
case class UpdatedVisionInfo(x: Int, y: Int, vision: Int)

case class UpdateUnitData(addedUnits: Map[Int, game.unit.Unit], updatedUnits: Map[Int, Map[String, String]],
                          deletedUnits: List[Int]) {
  def isEmpty: Boolean = addedUnits.isEmpty && updatedUnits.isEmpty && deletedUnits.isEmpty
}

case class UpdateData(addedUnits: Map[Int, game.unit.Unit], updatedUnits: Map[Int, Map[String, String]], deletedUnits: List[Int],
        playerStats: Map[String, String], addedTerrain: List[AddedTileInfo], changedTerrain: List[UpdatedTileInfo],
                       updatedVisionInfo: List[UpdatedVisionInfo]) {

//      todo fogOfWar  addedVision: List[(Int, Int)], removedVision: List[(Int, Int)]
  def isEmpty: Boolean = {
    addedUnits.isEmpty && updatedUnits.isEmpty && deletedUnits.isEmpty && playerStats.isEmpty &&
      addedTerrain.isEmpty && changedTerrain.isEmpty && updatedVisionInfo.isEmpty
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
  implicit val updateVisionInfoWrites = Json.writes[UpdatedVisionInfo]

  implicit val updateDataWrites = new Writes[UpdateData] {
    def writes(o: UpdateData): JsValue = {
      var map = Map[String, JsValueWrapper]()

      if (!o.addedUnits.isEmpty) map += ("addedUnits" -> o.addedUnits)
      if (!o.updatedUnits.isEmpty) map += ("updatedUnits" -> o.updatedUnits)
      if (!o.deletedUnits.isEmpty) map += ("deletedUnits" -> o.deletedUnits)

      if (!o.playerStats.isEmpty) map += ("playerStats" -> o.playerStats)

      if (!o.addedTerrain.isEmpty) map += ("addedTerrain" -> o.addedTerrain)
      if (!o.changedTerrain.isEmpty) map += ("changedTerrain" -> o.changedTerrain)
      if (!o.updatedVisionInfo.isEmpty) map += ("updatedVisionInfo" -> o.updatedVisionInfo)

      Json.obj(map.toSeq:_*)
    }
  }
}