package models.unit

import models.action.{CostParam, ActionEnum}

class TownHall extends Building {
  def hp = 1200

  override def actions = super.actions +
    ((ActionEnum.TRAIN, Some(classOf[Peasant])) -> Some(CostParam(400, 0, 0, 45))) +
    ((ActionEnum.UPGRADE, Some(classOf[Keep])) -> Some(CostParam(2000, 1000, 200, 200)))

  def width: Int = 4
  def height: Int = 4
}

class Keep extends TownHall {
  override def hp = 1400

  override def sightRange = 3

  override def actions = super.actions - (ActionEnum.UPGRADE, Some(classOf[Keep])) +
    ((ActionEnum.UPGRADE, Some(classOf[Castle])) -> Some(CostParam(2500, 1200, 500, 200)))
}

class Castle extends Keep {
  override def hp = 1600

  override def sightRange: Int = 6

  override def actions = super.actions - (ActionEnum.UPGRADE, Some(classOf[Castle]))
}
