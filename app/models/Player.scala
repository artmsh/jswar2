package models

import core.Race
import format.pud.PlayerType

class Player(val number: Int, val _type: PlayerType, val race: Race.Race, startingRes: (Int, Int, Int),
             var startPos: (Int, Int)) {
  var gold: Int = startingRes._1
  var lumber: Int = startingRes._2
  var oil: Int = startingRes._3
  val upgrades = Set[Int]()
}
