package models.unit

import models.action._
import models.action.MapTargetParam
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

abstract class Swordsman extends LandTroop with Patrolable {
  def hp = 60
  def armor = 2

  def moveSpeed: Int = 10
  def sightRange: Int = 4
}

class Footman extends Swordsman { type T = Human.type }
class Grunt extends Swordsman { type T = Orc.type }

abstract class Bowman extends LandTroop with Patrolable {
  def hp: Int = 40
  def armor: Int = 0

  def moveSpeed: Int = 10
  def sightRange: Int = 5
}

class Archer extends Bowman { type T = Human.type }
class Axethrower extends Bowman { type T = Orc.type }

abstract class AbstractCatapult extends LandTroop with Patrolable {
  def hp: Int = 110
  def armor: Int = 0

  def moveSpeed: Int = 5
  def sightRange: Int = 9
}

class Ballista extends AbstractCatapult { type T = Human.type }
class Catapult extends AbstractCatapult { type T = Orc.type }
