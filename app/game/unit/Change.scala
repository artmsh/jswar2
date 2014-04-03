package game.unit

import game.Order

trait Change
case class UnitPositionChange(unit: Unit, position: (Int, Int))
case class UnitActionsChange(unit: Unit, actions: Unit#ActionsType)
// case class UnitOrderChange(unit: Unit, order: Order)
case class UnitOccupyCell(unit: Unit, position: (Int, Int))