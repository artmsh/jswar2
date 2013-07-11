package models.unit

import models.action._
import models.action.MapTargetParam
import scala.Some

abstract class Troop[T <: Race] extends Unit[T] with Movable[T] {
  def isBuilding: Boolean = false
  def width: Int = 1
  def height: Int = 1
}

abstract class LandTroop[T <: Race] extends Troop[T] {
  override def actions = super.actions + ((ActionEnum.MOVE, None) -> Some(MapTargetParam(new MapSelector(None).passable)))
}

abstract class AirTroop[T <: Race] extends Troop[T] {
  override def actions = super.actions + ((ActionEnum.MOVE, None) -> Some(MapTargetParam(new MapSelector(None))))
}

abstract class SeaTroop[T <: Race] extends Troop[T] {
  override def actions = super.actions + ((ActionEnum.MOVE, None) -> Some(MapTargetParam(new MapSelector(None).water)))
}

abstract class Swordsman[T <: Race] extends LandTroop[T] with Patrolable[T] {
  def hp = 60
  def armor = 2

  def moveSpeed: Int = 10
  def sightRange: Int = 4
}

class Footman extends Swordsman[Human.type]
class Grunt extends Swordsman[Orc.type]

abstract class Bowman[T <: Race] extends LandTroop[T] with Patrolable[T] {
  def hp: Int = 40
  def armor: Int = 0

  def moveSpeed: Int = 10
  def sightRange: Int = 5
}

class Archer extends Bowman[Human.type]
class Axethrower extends Bowman[Orc.type]

abstract class AbstractCatapult[T <: Race] extends LandTroop[T] with Patrolable[T] {
  def hp: Int = 110
  def armor: Int = 0

  def moveSpeed: Int = 5
  def sightRange: Int = 9
}

class Ballista extends AbstractCatapult[Human.type]
class Catapult extends AbstractCatapult[Orc.type]
