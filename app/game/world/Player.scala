package game.world

import format.pud.{Pud, PudCodec}
import game.{Control, Race}

class Player(val race: Race, val control: Control, startingRes: (Int, Int, Int), val mapWidth: Int, val mapHeight: Int,
             val startPos: PudCodec.Position, pudUnits: List[PudCodec.Unit], val unitTypes: Pud#UnitTypes, val pudNumber: Int) {
  val gold: Int = startingRes._1
  val lumber: Int = startingRes._2
  val oil: Int = startingRes._3
  val upgrades = Set[Upgrade]()
  var units: List[game.unit.Unit] = pudUnits map { pu => game.unit.Unit(pu, this) }
  var seenPositions: Set[TileVisibility] = units
    // todo handle
    //  1 1 4      1 1 4
    //  1 4 4  =>  1 1 4
    //  1 1 4      1 1 4
    // case
    .flatMap { u => u.getVisibility filter { vis => vis.checkBounds(mapWidth, mapHeight) } }
    .groupBy { v: TileVisibility => (v.x, v.y) }
    .toSeq
    // reduce 2 visibilities with same location
    .map { e => e._2.sortBy(_.visibility).head }
    .toSet

  def diff(ps: Player): Map[String, String] =
  // todo upgrades
    ((List(gold, lumber, oil) zip List(ps.gold, ps.lumber, ps.oil)) zip List("gold", "lumber", "oil") filter {
      case ((g0, g1), name) => g0 - g1 > 0
    } map {
      case ((g0, g1), name) => name -> String.valueOf(g0 - g1)
    }).toMap

  def asMap: Map[String, String] = Map("gold" -> String.valueOf(gold), "lumber" -> String.valueOf(lumber), "oil" -> String.valueOf(oil))
}
