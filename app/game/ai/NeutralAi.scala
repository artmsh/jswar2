package game.ai

import format.pud.Pud
import akka.actor.ActorRef
import game.world.UpdateData
import models.unit.Critter

// handle critters only
class NeutralAi(unitTypes: Pud#UnitTypes, gameActor: ActorRef) extends Ai {
  var critters: List[Unit] = List()

  def update(data: UpdateData) {
    critters :+ (data.addedUnits.filter { case (n, p) => p.ch == Critter } map { case (n, p) => p }).toList
  }
}
