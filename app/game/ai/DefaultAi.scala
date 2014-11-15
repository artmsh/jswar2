package game.ai

import format.pud.{PudCodec, Pud}
import game.{Order, Race}
import game.unit.Change
import game.world.Player

class DefaultAi(val player: Player) extends Ai {
  def init(race: Race, types: Pud#UnitTypes, mapWidth: Int, mapHeight: Int, startPos: PudCodec.Position, pudNumber: Int) = ???
  def update(changes: List[Change]): List[(Int, Order)] = ???
}
