package game

import game.PlayerActor._
import game.ai.Ai
import game.unit.Change
import game.world.Player

object PlayerActor {
  case object Init
  case object InitOk
  case object InitFailed

  case class MakeOrders(player: Player, actions: List[(Int, Order)])
  case class DoActionError(message: String)

  case class UpdateFirstTime(terrain: Game#Terrain)
  case class Update(changes: List[Change])
}

class PlayerActor(val player: Player, ai: Ai) extends AbstractPlayerActor {
  override def receive: Receive = {
    case Init =>
      ai.init(player.race, player.unitTypes, player.mapWidth, player.mapHeight, player.startPos, player.pudNumber)
      context.become(gameLoop(ai))
      sender ! InitOk
    case _ => Unit
  }

  def gameLoop(ai: Ai): Receive = {
    case UpdateFirstTime(terrain) =>
      val orders: List[(Int, Order)] = ai.update(updateForFirstTime(terrain))
      sender ! MakeOrders(player, orders)

    case Update(changes) =>
      val orders: List[(Int, Order)] = ai.update(changes)
      sender ! MakeOrders(player, orders)
  }
}
