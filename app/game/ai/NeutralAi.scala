package game.ai

import akka.actor.ActorRef
import format.pud.Pud
import game.world.UpdateData

// handle critters only
class NeutralAi(unitTypes: Pud#UnitTypes, gameActor: ActorRef) extends Ai {
  var critters: List[Unit] = List()

  def update(data: UpdateData) {
    critters :+ (data.addedUnits.filter { case (n, p) => p.name == "unit-critter" } map { case (n, p) => p }).toList
  }
}
