package core.unit

import core.action._
import core.action.MapTargetParam
import scala.Some

abstract class Troop extends Unit with Movable {
  def isBuilding: Boolean = false
  def width: Int = 1
  def height: Int = 1
}

abstract class LandTroop extends Troop {
  override def actions = super.actions + ((ActionEnum.MOVE, None) -> Some(MapTargetParam(new MapSelector(None).passable)))
}

abstract class AirTroop extends Troop {
  override def actions = super.actions + ((ActionEnum.MOVE, None) -> Some(MapTargetParam(new MapSelector(None))))
}

abstract class SeaTroop extends Troop {
  override def actions = super.actions + ((ActionEnum.MOVE, None) -> Some(MapTargetParam(new MapSelector(None).water)))
}

class Footman extends LandTroop with Patrolable {
  def hp = 60
  def armor = 2

  def moveSpeed: Int = 10
  def sightRange: Int = 4
}

class Archer extends Troop {
  def hp: Int = 40
  def armor: Int = 0
  def sightRange: Int = 5
}

class Ballista extends Troop {
  def hp: Int = 110
  def armor: Int = 0
  def sightRange: Int = 9
}
