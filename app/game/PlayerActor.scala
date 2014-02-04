package game

import akka.actor.Actor
import models.unit.UnitCharacteristic
import world.UpdateData
import game.PlayerActor.{InitOk, Init}

object PlayerActor {
  case class Init(unitTypes: Vector[(String, UnitCharacteristic)], startPos: (Int, Int), race: Race)
  case object InitOk

  case class DoAction(actions: List[(Int, Order)])
  case class DoActionError(message: String)

  case class Update(updateData: UpdateData)
}

class PlayerActor(playerNum: Int) extends Actor {
  override def receive: Receive = {
    // todo fix
    case Init(unitTypes, startPos, race) => sender ! InitOk
    case _ => Unit
  }
}
