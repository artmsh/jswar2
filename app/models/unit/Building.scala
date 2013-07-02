package models.unit

import models.action.{RequireParam, CompositeParam, CostParam, ActionEnum}

abstract class Building extends Unit {
  def isBuilding: Boolean = true
  def armor: Int = 20
  def sightRange: Int = 1
}

abstract class AbstractFarm extends Building {
  def hp: Int = 400
  def width: Int = 2
  def height: Int = 2
  override def sightRange: Int = 2
}

class Farm extends AbstractFarm { type T = Human.type }
class PigFarm extends AbstractFarm { type T = Orc.type }

abstract class Barracks extends Building {
  def hp: Int = 800
  def width: Int = 3
  def height: Int = 3

  override def actions = super.actions +
    ((ActionEnum.TRAIN, Some(classOf[Swordsman#T])) -> Some(CostParam(600, 0, 0, 60))) +
    ((ActionEnum.TRAIN, Some(classOf[Bowman#T])) ->
        Some(CompositeParam(CostParam(500, 50, 0, 70), RequireParam(classOf[LumberMill#T])))) +
    ((ActionEnum.TRAIN, Some(classOf[AbstractCatapult#T])) ->
        Some(CompositeParam(CostParam(900, 300, 0, 250), RequireParam(classOf[Blacksmith#T]))))
}

class HumanBarracks extends Barracks { type T = Human.type }
class OrcBarracks extends Barracks { type T = Orc.type }

abstract class LumberMill extends Building {
  def hp: Int = 600
  def width: Int = 3
  def height: Int = 3
}

class ElvenLumberMill extends LumberMill { type T = Human.type }
class TrollLumberMill extends LumberMill { type T = Orc.type }

abstract class Blacksmith extends Building {
  def hp: Int = 775
  def width: Int = 3
  def height: Int = 3
}

class HumanBlacksmith extends Blacksmith { type T = Human.type }
class OrcBlacksmith extends Blacksmith { type T = Orc.type }

abstract class WatchTower extends Building {
  def hp: Int = 100
  def width: Int = 2
  def height: Int = 2

  override def sightRange = 9
}

class HumanWatchTower extends WatchTower { type T = Human.type }
class OrcWatchTower extends WatchTower { type T = Orc.type }