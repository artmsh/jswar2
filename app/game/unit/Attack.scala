package game.unit

import game.Order
import game.world.World

case class Attack(unit: Unit, order: Order, ticksLeft: Int) extends AtomicAction {
  def spentTick(world: World, rest: Unit#ActionsType): Set[_ >: Change] = ???

  // change don't include order changes
  def isChanged(aa: AtomicAction): Boolean = ???

  def change: Set[(String, String)] = ???
}
