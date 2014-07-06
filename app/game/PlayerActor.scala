package game

import ai.{NeutralAi, Ai}
import akka.actor.{ActorRef, Actor}
import models.unit.UnitCharacteristic
import world.UpdateData
import game.PlayerActor.{Update, InitOk, Init}
import format.pud.Pud

object PlayerActor {
  case class Init(unitTypes: Pud#UnitTypes, startPos: (Int, Int), race: Race)
  case object InitOk

  case class DoAction(actions: List[(Int, Order)])
  case class DoActionError(message: String)

  case class Update(updateData: UpdateData)
}

class PlayerActor(playerNum: Int) extends Actor {
  override def receive: Receive = {
    // todo fix
    case Init(unitTypes, startPos, race) =>
      if (race == Neutral) {
        context.become(gameLoop(new NeutralAi(unitTypes, context.sender)))
      }
      sender ! InitOk
    case _ => Unit
  }

  def gameLoop(ai: Ai): Receive = {
    case Update(updateData: UpdateData) => ai.update(updateData)
  }
}
