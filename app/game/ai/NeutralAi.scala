package game.ai

import format.pud.{PudCodec, Pud}
import game.{Race, Order}
import game.unit.Change
import game.world.Player

// handle critters only
class NeutralAi(player: Player) extends Ai {
  var critters: List[Unit] = List()

  def init(race: Race, types: Pud#UnitTypes, mapWidth: Int, mapHeight: Int, startPos: PudCodec.Position, pudNumber: Int) = ???

  def update(changes: List[Change]): List[(Int, Order)] = {
//    critters :+ (changes.filter { case (n, p) => p.name == "unit-critter" } map { case (n, p) => p }).toList
    List()
  }
}
