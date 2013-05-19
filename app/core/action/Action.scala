package core.action

import core.{Tile, unit}
import core.unit.Unit
import utils.MultiMap

object ActionEnum extends Enumeration {
  type Action = Value
  val MOVE, STOP, ATTACK, PATROL, HOLD, HARVEST, RETURNWITHGOODS, UPGRADE, TRAIN, CANCEL, REPAIR, BUILD, PREBUILD,
    CASTSPELL = Value
}

trait Movable extends unit.Unit {
  def moveSpeed: Int

  abstract override def actions = super.actions +
    ((ActionEnum.STOP, None) -> None)
}

trait Attackable extends unit.Unit {
  def basicDamage: Int
  def piercingDamage: Int
  def attackRange: Int

  abstract override def actions = super.actions + ((ActionEnum.ATTACK, None) -> None)
}

trait Patrolable extends Unit {
  abstract override def actions = super.actions + ((ActionEnum.PATROL, None) -> None)
}

trait Holdable extends Unit {
  override def actions = super.actions + ((ActionEnum.HOLD, None) -> None)
}

trait GoldGatherable extends unit.Unit {
  abstract override def actions = super.actions +
    ((ActionEnum.HARVEST, None) -> Some(ResourceParam(Gold))) +
    ((ActionEnum.RETURNWITHGOODS, None) -> Some(ResourceParam(Gold)))
}

trait LumberGatherable extends unit.Unit {
  abstract override def actions = super.actions +
    ((ActionEnum.HARVEST, None) -> Some(ResourceParam(Lumber))) +
    ((ActionEnum.RETURNWITHGOODS, None) -> Some(ResourceParam(Lumber)))
}

trait OilGatherable extends unit.Unit {
  abstract override def actions = super.actions +
    ((ActionEnum.HARVEST, None) -> Some(ResourceParam(Oil))) +
    ((ActionEnum.RETURNWITHGOODS, None) -> Some(ResourceParam(Oil)))
}

trait Resource
case object Gold extends Resource
case object Lumber extends Resource
case object Oil extends Resource

trait ActionParam
case class CostParam(gold: Int, lumber: Int, oil: Int, time: Int) extends ActionParam
case class ResourceParam(resource: Resource) extends ActionParam
case class CompositeParam(param1: ActionParam, param2: ActionParam) extends ActionParam
case class RequireParam(target: Class[_ <: unit.Unit]) extends ActionParam
case class MapTargetParam(selector : () => Seq[Tile]) extends ActionParam