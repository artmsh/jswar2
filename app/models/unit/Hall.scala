package models.unit

//import models.action.{CostParam, ActionEnum}
//
//abstract class Hall[T <: Race] extends Building[T] {
//  def hp = 1200
//
//  override def actions = super.actions +
//    ((ActionEnum.TRAIN, Some(classOf[Worker[T]])) -> Some(CostParam(400, 0, 0, 45))) +
//    ((ActionEnum.UPGRADE, Some(classOf[HallL2[T]])) -> Some(CostParam(2000, 1000, 200, 200)))
//
//  def width: Int = 4
//  def height: Int = 4
//}
//
//class TownHall extends Hall[Human.type]
//class GreatHall extends Hall[Orc.type]
//
//abstract class HallL2[T <: Race] extends Hall[T] {
//  override def hp = 1400
//
//  override def sightRange = 3
//
//  override def actions = super.actions - (ActionEnum.UPGRADE, Some(classOf[HallL2[T]])) +
//    ((ActionEnum.UPGRADE, Some(classOf[HallL3[T]])) -> Some(CostParam(2500, 1200, 500, 200)))
//}
//
//class Keep extends HallL2[Human.type]
//class Stronghold extends HallL2[Orc.type]
//
//abstract class HallL3[T <: Race] extends HallL2[T] {
//  override def hp = 1600
//
//  override def sightRange: Int = 6
//
//  override def actions = super.actions - (ActionEnum.UPGRADE, Some(classOf[HallL3[T]]))
//}
//
//class Castle extends HallL3[Human.type]
//class Fortress extends HallL3[Orc.type]