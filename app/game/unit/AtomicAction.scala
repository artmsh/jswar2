package game.unit

import game.{Game, Order}

trait AtomicAction {
  val ticksLeft: Int
  val unit: Unit
  val order: Order
  def execute(game: Game, rest: Unit#ActionsType): Set[Change]

  // change don't include order changes
  def isChanged(aa: AtomicAction): Boolean
  def change: Set[(String, String)]
}