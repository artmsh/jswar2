package core.unit

import core.action._
import scala.Some

class Peasant extends Troop with Movable with Attackable with GoldGatherable with LumberGatherable {
  def basicDamage: Int = 3
  def piercingDamage: Int = 2
  def attackRange: Int = 1

  def moveSpeed: Int = 10

  def hp: Int = 30
  def armor = 0

  override def actions = super.actions + ((ActionEnum.REPAIR, None) -> None) +
    ((ActionEnum.BUILD, Some(Class[TownHall])) -> Some(CostParam(1200, 800, 0, 255))) +
    ((ActionEnum.BUILD, Some(Class[Farm])) -> Some(CostParam(500, 250, 0, 100))) +
    ((ActionEnum.BUILD, Some(Class[Barracks])) -> Some(CostParam(700, 450, 0, 200))) +
    ((ActionEnum.BUILD, Some(Class[ElvenLumberMill])) -> Some(CostParam(600, 450, 0, 150))) +
    ((ActionEnum.BUILD, Some(Class[Blacksmith])) -> Some(CostParam(800, 450, 100, 200))) +
    ((ActionEnum.BUILD, Some(Class[WatchTower])) -> Some(CostParam(550, 200, 0, 60)))

  def sightRange: Int = 4
}
