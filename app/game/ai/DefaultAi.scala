package game.ai

import game.world.{UpdateData, Player}

class DefaultAi(val player: Player) extends Ai {
  override def update(data: UpdateData): Unit = ???
}
