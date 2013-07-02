package models.unit

import utils.MultiMap
import models.action.ActionEnum._
import models.action.ActionParam
import javax.script.{ScriptEngineManager, ScriptEngine}
import scala.io.Source
import sun.org.mozilla.javascript.internal.NativeArray
import scala.collection.JavaConversions._
import scala.util.Try

trait Unit {
  type T <: Race

  var x: Int = _
  var y: Int = _
  var player: Int = _
  var data: Int = _

  def actions: MultiMap[(Action, Option[Class[_ <: Unit#T]]), Option[ActionParam]] =
    new MultiMap(Map[(Action, Option[Class[_ <: Unit#T]]), Set[Option[ActionParam]]]())
  def isBuilding: Boolean
  def hp: Int
  def armor: Int
  def width: Int
  def height: Int
  def sightRange: Int
}

object Unit {
  val scriptEngine: ScriptEngine = new ScriptEngineManager().getEngineByName("js")
  scriptEngine.eval(Source.fromFile("public/javascripts/pud.js").bufferedReader())
  val unitScriptNames = scriptEngine.get("unitScriptNames").asInstanceOf[NativeArray].toIndexedSeq map (_.toString)

  def fromPudUnit(pudUnit: models.format.Unit): Option[Unit] = {
    (for {
      unitClass <- Try(Class.forName("models.unit." + toClassName(unitScriptNames(pudUnit.Type))))
      unit <- Try(unitClass.newInstance.asInstanceOf[Unit])
    } yield {
      unit.x = pudUnit.x
      unit.y = pudUnit.y
      unit.data = pudUnit.data
      unit.player = pudUnit.player

      unit
    }).toOption
  }

  // unit-pig-farm -> PigFarm
  def toClassName(s: String): String = {
    s.split("-").drop(1).reduceLeft(_ + _.capitalize)
  }
}

sealed trait Race
case object Human extends Race
case object Orc extends Race