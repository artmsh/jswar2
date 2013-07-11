package models.unit

import models.action._
import scala.Some

abstract class Worker[T <: Race] extends Troop[T] with Movable[T] with Attackable[T] with GoldGatherable[T] with LumberGatherable[T] {
  def basicDamage: Int = 3
  def piercingDamage: Int = 2
  def attackRange: Int = 1

  def moveSpeed: Int = 10

  def hp: Int = 30
  def armor = 0

  override def actions = super.actions + ((ActionEnum.REPAIR, None) -> None) +
    ((ActionEnum.BUILD, Some(classOf[Hall[T]])) -> Some(CostParam(1200, 800, 0, 255))) +
    ((ActionEnum.BUILD, Some(classOf[AbstractFarm[T]])) -> Some(CostParam(500, 250, 0, 100))) +
    ((ActionEnum.BUILD, Some(classOf[Barracks[T]])) -> Some(CostParam(700, 450, 0, 200))) +
    ((ActionEnum.BUILD, Some(classOf[LumberMill[T]])) -> Some(CostParam(600, 450, 0, 150))) +
    ((ActionEnum.BUILD, Some(classOf[Blacksmith[T]])) -> Some(CostParam(800, 450, 100, 200))) +
    ((ActionEnum.BUILD, Some(classOf[WatchTower[T]])) -> Some(CostParam(550, 200, 0, 60)))

  def sightRange: Int = 4
}

class Peasant extends Worker[Human.type]
class Peon extends Worker[Orc.type]