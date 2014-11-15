package game

import controllers.Tileset
import format.pud._
import game.unit.Change
import game.world.{Tile, Player}

class Game(val gameId: Int, val map: Pud, val tileset: Tileset.Value, peasantOnly: Boolean, playerSettings: List[(Int, Race, Control)]) {
  var players: List[Player] = playerSettings map {
    case (number, race, control) => new Player(race, control, map.startingRes(number), map.mapSizeX, map.mapSizeY,
      map.startingPos(number),
      map._pud.unit._2.units.filter(u => u.player == number).toList, map.unitTypes, number)
  }

  type Terrain = Vector[Vector[world.Tile]]
  var terrain: Terrain = map.tiles map { v: Vector[format.pud.Tile] => v map { Tile(_) } }

  var ticks: Int = 0

  def executeStep(orders: Map[Player, List[(Int, Order)]]): List[Change] = ???

  def getUnitsPassability(player: Player, kind: Kind): ((Int, Int)) => Boolean = kind match {
    case Fly => (p => true)
    case Land => {
//      // todo care on passability on non explored tiles
//      val vision = playerExploredTerrain(player)
//
//      p: (Int, Int) => vision(p._1)(p._2) != 0 && unitsOnMap(p._1)(p._2).fold(true)(!_.isBuilding)
      ???
    }
    // todo implement
    case Naval => ???
  }

  def unitsOnMap(y: Int)(x: Int): Option[unit.Unit] = ???
}
