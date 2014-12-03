package game.unit

import game.Game
import game.world._

sealed trait Change {
  def doChange(game: Game)
  def isAffectPlayer(player: Player): Boolean
}

case class UnitAdd(newUnit: game.unit.Unit) extends Change {
  override def doChange(game: Game) {
    newUnit.player.units = newUnit.player.units :+ newUnit
  }

  override def isAffectPlayer(player: Player): Boolean = Change.samePlayerOrSeen(player, newUnit)
}

case class UnitPositionChange(unit: Unit, newPosition: (Int, Int)) extends Change {
  override def doChange(game: Game) {
    game.occupiedLocations = game.occupiedLocations.updated(unit.y, game.occupiedLocations(unit.y).updated(unit.x, None))
    unit.x = newPosition._1
    unit.y = newPosition._2
  }

  override def isAffectPlayer(player: Player): Boolean = Change.samePlayerOrSeen(player, unit)
}

case class UnitActionsChange(unit: game.unit.Unit, actions: Unit#ActionsType) extends Change {
  override def doChange(game: Game) {
    unit.atomicAction = actions
  }

  override def isAffectPlayer(player: Player): Boolean = false
}

case class UnitHeadActionChange(unit: Unit, newAction: AtomicAction) extends Change {
  override def doChange(game: Game) {}

  override def isAffectPlayer(player: Player): Boolean = Change.samePlayerOrSeen(player, unit)
}

case class UnitOccupyCell(unit: game.unit.Unit, position: (Int, Int)) extends Change {
  override def doChange(game: Game) {
    assert(game.unitsOnMap(unit.y)(unit.x) == None)
    game.occupiedLocations = game.occupiedLocations.updated(unit.y, game.occupiedLocations(unit.y).updated(unit.x, Some(unit)))
  }

  override def isAffectPlayer(player: Player): Boolean = false
}

case class ResourcesChange(player: Player, goldDelta: Int, lumberDelta: Int, oilDelta: Int) extends Change {
  override def doChange(game: Game) {
    player.gold += goldDelta
    player.lumber += lumberDelta
    player.oil += oilDelta
  }

  override def isAffectPlayer(p: Player): Boolean = p == player
}

case class TerrainAdd(player: Player, tile: Tile, visibility: TileVisibility) extends Change {
  override def doChange(game: Game) {}

  override def isAffectPlayer(p: Player): Boolean = p == player
}

case class TerrainVisibilityChange(player: Player, newVisibility: TileVisibility) extends Change {
  override def doChange(game: Game) {}

  override def isAffectPlayer(p: Player): Boolean = p == player
}

object Change {
  def isPlayerSeenUnit(player: Player, unit: game.unit.Unit): Boolean =
    player.seenPositions.exists { case TileVisibility(x, y, vis: Visibility) => unit.x == x && unit.y == y && vis != NonVisible }

  def samePlayerOrSeen(player: Player, unit: game.unit.Unit): Boolean =
    unit.player == player || Change.isPlayerSeenUnit(player, unit)
}