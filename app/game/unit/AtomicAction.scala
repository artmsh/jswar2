package game.unit

import game.Order
import game.world.World

trait AtomicAction {
  val ticksLeft: Int
  val unit: Unit
  val order: Order
  def spentTick(world: World, rest: Unit#ActionsType): Set[_ >: Change]

  // change don't include order changes
  def isChanged(aa: AtomicAction): Boolean
  def change: Set[(String, String)]
}