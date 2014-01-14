package format.pud

import scodec._
import scodec.Codecs._
import models.unit._
import scalaz.\/
import shapeless.{HNil, HList}

class UnitCharacteristicCodec extends Codec[IndexedSeq[UnitCharacteristic]] {
  val unitCharacteristicCodec = (
      ("sightRange" | uint32L) ::
      ("hitPoints" | uint16L) ::
      ("magic" | bool) ::
      ("buildTime" | uint8) ::
      ("tenthGoldCost" | uint8) ::
      ("tenthLumberCost" | uint8) ::
      ("tenthOilCost" | uint8) ::
      ("unitSize" | (uint16L ~ uint16L)) ::
      ("boxSize" | (uint16L ~ uint16L)) ::
      ("attackRange" | uint8) ::
      ("reactRangeComputer" | uint8) ::
      ("reactRangeHuman" | uint8) ::
      ("armor" | uint8) ::
      ("selectableViaRectangle" | bool) ::
      ("priority" | uint8) ::
      ("basicDamage" | uint8) ::
      ("piercingDamage" | uint8) ::
      ("weaponsUpgradable" | uint8) ::
      ("armorUpgradable" | uint8) ::
      // todo implement
      ("missileWeapon" | uint8) ::
      ("unitKind" | uint8) ::
      ("decayRate" | uint8) ::
      ("annoyComputer" | uint8) ::
      ("2ndmouseButtonAction" | uint8) ::
      ("pointForKilling" | uint16L) ::
      ("canTarget" | uint8) ::
      ("flags" | uint32L)
  )

  val dataSliceSizes = Vector(
    110 * 4,
    110 * 2,
    110,
    110,
    110,
    110,
    110,
    110 * 4,
    110 * 4,
    110,
    110,
    110,
    110,
    110,
    110,
    110,
    110,
    110,
    110,
    110,
    110,
    110,
    110,
    58,
    110 * 2,
    110,
    110 * 4
  )

  def encode(a: IndexedSeq[UnitCharacteristic]): \/[scodec.Error, BitVector] = ???

  def decode(bits: BitVector): \/[scodec.Error, (BitVector, IndexedSeq[UnitCharacteristic])] = {
    // drop obsolete data
    var bytes = bits.toByteArray.drop(110 * 2 + 508 * 2)
    var hlists: IndexedSeq[Array[Byte]] = Array.fill(110)(Array[Byte]())
    dataSliceSizes.foreach(n => if (n % 110 == 0) {
      val slice = bytes.take(n)
      hlists = hlists.zip(slice.sliding(n / 110, n / 110).toArray) map { a => a._1 ++ a._2 }
      bytes = bytes.drop(n)
    } else {
      val slice = bytes.take(n) ++ Array.fill[Byte](52)(0)
      hlists = hlists.zip(slice) map { a => a._1 :+ a._2 }
      bytes = bytes.drop(n)
    })

    new IndexedSeqCodec(unitCharacteristicCodec).decode(BitVector(hlists.foldLeft(Array[Byte]())(_ ++ _))) map { t =>
      (t._1, t._2 map { hl =>
        val params = hl.toArray[AnyRef]
        new UnitCharacteristic {
          val sightRange: Long = params(0).asInstanceOf[Long]
          val hitPoints: Int = params(1).asInstanceOf[Int]
          val isMagic: Boolean = params(2).asInstanceOf[Boolean]
          val buildTime: Int = params(3).asInstanceOf[Int]
          val goldCost: Int = params(4).asInstanceOf[Int] * 10
          val lumberCost: Int = params(5).asInstanceOf[Int] * 10
          val oilCost: Int = params(6).asInstanceOf[Int] * 10
          val unitSize: (Int, Int) = params(7).asInstanceOf[(Int, Int)]
          val boxSize: (Int, Int) = params(8).asInstanceOf[(Int, Int)]
          val attackRange: Int = params(9).asInstanceOf[Int]
          val reactionRangeComputer: Int = params(10).asInstanceOf[Int]
          val reactionRangeHuman: Int = params(11).asInstanceOf[Int]
          val armor: Int = params(12).asInstanceOf[Int]
          val selectableViaRectangle: Boolean = params(13).asInstanceOf[Boolean]
          val priority: Int = params(14).asInstanceOf[Int]
          val basicDamage: Int = params(15).asInstanceOf[Int]
          val piercingDamage: Int = params(16).asInstanceOf[Int]
          val weaponsUpgradable: Boolean = params(17).asInstanceOf[Boolean]
          val armorUpgradable: Boolean = params(18).asInstanceOf[Boolean]
          val missileWeapon: Option[Missile] = Missile(params(19).asInstanceOf[Int])
          val kind: Kind = Kind(params(20).asInstanceOf[Int])
          val decayRate: Int = params(21).asInstanceOf[Int]
          val annoyFactor: Int = params(22).asInstanceOf[Int]
          val secondMouseBtnAction: Option[MouseBtnAction] = MouseBtnAction(params(23).asInstanceOf[Int])
          val pointsForKilling: Int = params(24).asInstanceOf[Int]
          val canTarget: CanTarget = new CanTarget(params(25).asInstanceOf[Int])
          val flags: Long = params(22).asInstanceOf[Long]
        }
      })
    }
  }
}
