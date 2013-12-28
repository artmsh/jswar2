package models.unit

import utils.MultiMap
import models.action.ActionEnum._
import models.action.{Still, StillOrder, AtomicAction, ActionParam}
import javax.script.{ScriptEngineManager, ScriptEngine}
import scala.io.Source
import sun.org.mozilla.javascript.internal.NativeArray
import scala.collection.JavaConversions._

trait Unit[T <: Race] {
  var x: Int = _
  var y: Int = _
  var player: Int = _
  var data: Int = _

  var atomicAction: AtomicAction = new Still
  var order: Option[Action] = None

  def actions: MultiMap[(Action, Option[Class[_ <: Unit[T]]]), Option[ActionParam]] =
    new MultiMap(Map[(Action, Option[Class[_ <: Unit[T]]]), Set[Option[ActionParam]]]())
  def isBuilding: Boolean
  def hp: Int
  def armor: Int
  def width: Int
  def height: Int
  def sightRange: Int

  /** in original warcraft 2 sight center of unit appears at bottom right center of unit sprite */
  def centerCoords = ((x + width) * 32 - 16, (y + height) * 32 - 16)
}

object Unit {
  val scriptEngine: ScriptEngine = new ScriptEngineManager().getEngineByName("js")
  scriptEngine.eval(Source.fromFile("public/javascripts/pud.js").bufferedReader())
  val unitScriptNames = scriptEngine.get("unitScriptNames").asInstanceOf[NativeArray].toIndexedSeq map (_.toString)

  def fromPudUnit(pudUnit: models.format.Unit): Option[Unit[_ <: Race]] = {
    try {
      val unitClass = Class.forName("models.unit." + toClassName(unitScriptNames(pudUnit.Type)))
      val unit = unitClass.newInstance.asInstanceOf[Unit[_ <: Race]]

      unit.x = pudUnit.x
      unit.y = pudUnit.y
      unit.data = pudUnit.data
      unit.player = pudUnit.player

      Some(unit)
    } catch {
      case e: Exception => println(e); None
    }
  }

  // unit-pig-farm -> PigFarm
  def toClassName(unitType: String): String = {
    unitType.split("-").drop(1).foldLeft("")(_ + _.capitalize)
  }
}

sealed trait Race
case object Human extends Race
case object Orc extends Race
case object Neutral extends Race