package format.pud

import java.io.FileInputStream
import scodec.{Codec, BitVector}
import collection.immutable.Stream
import format.pud.PudCodec._
import core.Tileset
import models.unit.UnitCharacteristic
import scalaz.\/
import game.unit

object Pud {
  def apply(mapFileName: String): Option[Pud] = {
    val inputStream = new FileInputStream(mapFileName)
    val bits = BitVector(Stream.continually(inputStream.read).takeWhile(i => i != -1).map(_.toByte).toArray)
    val map: \/[scodec.Error, Pud] = Codec.decode[_Pud](bits).map(p => new Pud(p, mapFileName))
    map.leftMap[scala.Unit](e => println(e))
    map.toOption
  }
}

class Pud(val _pud: _Pud, val filename: String) {
  val description = _pud.desc._2.description.trim

  val mapSizeX = _pud.dim._2.x
  val mapSizeY = _pud.dim._2.y
  val tileset = Tileset(_pud.era._2.terrain + 2) // adjustment for random values
  val players = _pud.ownr._2.playerSlots
  val startingRes = _pud.sgld._2.gold.zip(_pud.slbr._2.lumber).zip(_pud.soil._2.oil) map { p => (p._1._1, p._1._2, p._2) }
  val startingPos = _pud.unit._2.units
    .filter(_.isStartLocation)
    .sortWith(_.player < _.player)
    .map(u => (u.x, u.y))
  val numPlayers = _pud.ownr._2.playerSlots.count(p => p != Nobody && p != Neutral)
  val tiles = _pud.mtxm._2
  val aiType: Array[AiType] = Array()

  val unitCharacteristics: Vector[(String, UnitCharacteristic)] = {
    _pud.udta._2.unitCharacteristics.zip(unit.defaults).map(t => (t._2._1, t._1)).toVector
  }
}
