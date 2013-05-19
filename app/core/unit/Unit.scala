package core.unit

import utils.MultiMap
import core.action.ActionEnum._
import core.action.ActionParam

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