package core.unit

import core.action.{RequireParam, CompositeParam, CostParam, ActionEnum}

abstract class Building extends Unit {
  def isBuilding: Boolean = true
  def armor: Int = 20
  def sightRange: Int = 1
}

class Farm extends Building {
  def hp: Int = 400
  def width: Int = 2
  def height: Int = 2
  override def sightRange: Int = 2
}

class Barracks extends Building {
  def hp: Int = 800
  def width: Int = 3
  def height: Int = 3

  override def actions = super.actions +
    ((ActionEnum.TRAIN, Some(classOf[Footman])) -> Some(CostParam(600, 0, 0, 60))) +
    ((ActionEnum.TRAIN, Some(classOf[Archer])) -> Some(CompositeParam(CostParam(500, 50, 0, 70), RequireParam(classOf[ElvenLumberMill]))))
  ((ActionEnum.TRAIN, Some(classOf[Ballista])) -> Some(CompositeParam(CostParam(900, 300, 0, 250), RequireParam(classOf[Blacksmith]))))
}

class ElvenLumberMill extends Building {
  def hp: Int = 600
  def width: Int = 3
  def height: Int = 3
}

class Blacksmith extends Building {
  def hp: Int = 775
  def width: Int = 3
  def height: Int = 3
}

class WatchTower extends Building {
  def hp: Int = 100
  def width: Int = 2
  def height: Int = 2

  override def sightRange = 9
}
