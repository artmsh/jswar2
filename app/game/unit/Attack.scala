package game.unit

import game.{Game, Order}

case class Attack(unit: Unit, order: Order, ticksLeft: Int) extends AtomicAction {
  def execute(game: Game, rest: Unit#ActionsType): Set[Change] = ???

  // change don't include order changes
  def isChanged(aa: AtomicAction): Boolean = ???

  def change: Set[(String, String)] = ???
}
