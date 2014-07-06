package game.ai

import game.world.UpdateData

trait Ai {
  def update(data: UpdateData)
}
