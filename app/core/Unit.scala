package core

import javax.script.{ScriptEngineManager, ScriptEngine}
import io.Source
import sun.org.mozilla.javascript.internal.{NativeObject, NativeArray}

class Unit(ud: utils.Unit, val player: Player) {
  val x = ud.x
  val y = ud.y
  val uname = Unit.getUnitName(ud.Type)
  val utype: Map[String, AnyRef] = Unit.getUnitType(uname)

  /** in original warcraft 2 sight center of unit appears at bottom right center of unit sprite */
  def centerCoords = ((this.x + this.width) * 32  - 16, (this.y + this.height) * 32 - 16)

  def sightRange() = utype("SightRange").asInstanceOf[Double].toInt
  def height() = utype("TileSize").asInstanceOf[NativeArray].get(0, null).asInstanceOf[Double].toInt
  def width() = utype("TileSize").asInstanceOf[NativeArray].get(1, null).asInstanceOf[Double].toInt
}

object Unit {
  val scriptEngine: ScriptEngine = new ScriptEngineManager().getEngineByName("js")
  scriptEngine.eval(Source.fromFile("public/javascripts/pud.js").bufferedReader())
  scriptEngine.eval("var units = {}")
  scriptEngine.eval("var UnitTypeFiles = {}")
  scriptEngine.eval(Source.fromFile("public/javascripts/units.js").bufferedReader())
  scriptEngine.eval(Source.fromFile("public/javascripts/human/units.js").bufferedReader())
  scriptEngine.eval(Source.fromFile("public/javascripts/orc/units.js").bufferedReader())

  def getUnitName(i: Int): String = {
    val unitNames: NativeArray = scriptEngine.get("unitScriptNames").asInstanceOf[NativeArray]
    unitNames.get(i, null).asInstanceOf[String]
  }

  def getUnitType(name: String): Map[String, AnyRef] = {
    val units: NativeObject = scriptEngine.get("units").asInstanceOf[NativeObject]
    val unit: NativeObject = units.get(name, null).asInstanceOf[NativeObject]
    unit.getAllIds .map { o => o.toString } .foldLeft(Map[String, AnyRef]()) { (m, id) => m + (id -> unit.get(id, null)) }
  }
}