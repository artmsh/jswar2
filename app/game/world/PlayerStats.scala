package game.world

import format.pud.PlayerType
import game.Race

class PlayerStats(val number: Int, val _type: PlayerType, val race: Race, startingRes: (Int, Int, Int),
             var startPos: (Int, Int)) {
  var gold: Int = startingRes._1
  var lumber: Int = startingRes._2
  var oil: Int = startingRes._3
  val upgrades = Set[Int]()
}
