package game.world

import format.pud.PlayerType
import game.Race

class PlayerStats(val number: Int, val _type: PlayerType, val race: Race, startingRes: (Int, Int, Int),
             val startPos: (Int, Int)) {
  val gold: Int = startingRes._1
  val lumber: Int = startingRes._2
  val oil: Int = startingRes._3
  val upgrades = Set[Upgrade]()

  def updated(gold: Int, lumber: Int, oil: Int): PlayerStats =
    new PlayerStats(number, _type, race, (gold, lumber, oil), startPos)

  def diff(ps: PlayerStats): Map[String, String] =
  // todo upgrades
    ((List(gold, lumber, oil) zip List(ps.gold, ps.lumber, ps.oil)) zip List("gold", "lumber", "oil") filter {
      case ((g0, g1), name) => g0 - g1 > 0
    } map {
      case ((g0, g1), name) => name -> String.valueOf(g0 - g1)
    }).toMap
}
