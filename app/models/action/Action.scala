package models.action



//object ActionEnum extends Enumeration {
//  type Action = Value
//  val MOVE, STOP, ATTACK, PATROL, HOLD, HARVEST, RETURNWITHGOODS, UPGRADE, TRAIN, CANCEL, REPAIR, BUILD, PREBUILD,
//    CASTSPELL = Value
//}
//
//trait Movable[T <: Race] extends Unit[T] {
//  def moveSpeed: Int
//  def moveTime: Int
//
//  abstract override def actions = super.actions +
//    ((ActionEnum.STOP, None) -> None)
//}
//
//trait Attackable[T <: Race] extends Unit[T] {
//  def basicDamage: Int
//  def piercingDamage: Int
//  def attackRange: Int
//
//  abstract override def actions = super.actions + ((ActionEnum.ATTACK, None) -> None)
//}
//
//trait Patrolable[T <: Race] extends Unit[T] {
//  abstract override def actions = super.actions + ((ActionEnum.PATROL, None) -> None)
//}
//
//trait Holdable[T <: Race] extends Unit[T] {
//  override def actions = super.actions + ((ActionEnum.HOLD, None) -> None)
//}
//
//trait GoldGatherable[T <: Race] extends Unit[T] {
//  abstract override def actions = super.actions +
//    ((ActionEnum.HARVEST, None) -> Some(ResourceParam(Gold))) +
//    ((ActionEnum.RETURNWITHGOODS, None) -> Some(ResourceParam(Gold)))
//}
//
//trait LumberGatherable[T <: Race] extends Unit[T] {
//  abstract override def actions = super.actions +
//    ((ActionEnum.HARVEST, None) -> Some(ResourceParam(Lumber))) +
//    ((ActionEnum.RETURNWITHGOODS, None) -> Some(ResourceParam(Lumber)))
//}
//
//trait OilGatherable[T <: Race] extends Unit[T] {
//  abstract override def actions = super.actions +
//    ((ActionEnum.HARVEST, None) -> Some(ResourceParam(Oil))) +
//    ((ActionEnum.RETURNWITHGOODS, None) -> Some(ResourceParam(Oil)))
//}
//
//trait Resource
//case object Gold extends Resource
//case object Lumber extends Resource
//case object Oil extends Resource
//
//trait ActionParam
//case class CostParam(gold: Int, lumber: Int, oil: Int, time: Int) extends ActionParam
//case class ResourceParam(resource: Resource) extends ActionParam
//case class CompositeParam(param1: ActionParam, param2: ActionParam) extends ActionParam
//case class RequireParam[T <: Race](target: Class[_ <: Unit[T]]) extends ActionParam
//case class MapTargetParam(selector : () => Seq[Tile]) extends ActionParam