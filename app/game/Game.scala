package game

import controllers.Tileset
import format.pud.Pud
import game.world.{Player, Terrain, World}

class Game(val map: Pud, val tileset: Tileset.Value, peasantOnly: Boolean, playerSettings: List[(Int, Race, Control)]) {
  var players: List[Player] = playerSettings map {
    case (number, race, control) => new Player(race, control, map.startingRes(number), map.startingPos(number),
      map._pud.unit._2.units.filter(u => u.player == number).toList, map.unitTypes)
  }
  var world = new World(Map(), Vector(), new Terrain(Vector(), 0, 0))
  var ticks: Int = 0

}
