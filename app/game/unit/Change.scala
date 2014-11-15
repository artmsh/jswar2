package game.unit

import game.world.{TileVisibility, Tile}

sealed trait Change
case class UnitAdd(newUnit: Unit) extends Change
case class UnitPositionChange(unit: Unit, newPosition: (Int, Int)) extends Change
case class UnitActionsChange(unit: Unit, actions: Unit#ActionsType) extends Change
case class UnitOccupyCell(unit: Unit, position: (Int, Int)) extends Change
case class ResourcesChange(newGold: Int, newLumber: Int, newOil: Int) extends Change
case class TerrainAdd(tile: Tile, visibility: TileVisibility) extends Change