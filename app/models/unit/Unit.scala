package models.unit

import utils.MultiMap
import models.action.ActionEnum._
import models.action.ActionParam

trait Unit {
  def actions: MultiMap[(Action, Option[Class[_ <: Unit]]), Option[ActionParam]] =
    new MultiMap(Map[(Action, Option[Class[_ <: Unit]]), Set[Option[ActionParam]]]())
  def isBuilding: Boolean
  def hp: Int
  def armor: Int
  def width: Int
  def height: Int
  def sightRange: Int
}