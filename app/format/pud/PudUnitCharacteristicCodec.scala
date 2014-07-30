package format.pud

import scodec._
import scodec.Codecs._
import models.unit._
import scalaz.\/
import shapeless._
import HList._

class PudUnitCharacteristicCodec extends Codec[IndexedSeq[PudUnitCharacteristic]] {
  val unitCharacteristicCodec = (
      ("sightRange" | uint32L) ::
      ("hitPoints" | uint16L) ::
      ("magic" | uint8) ::
      ("buildTime" | uint8) ::
      ("tenthGoldCost" | uint8) ::
      ("tenthLumberCost" | uint8) ::
      ("tenthOilCost" | uint8) ::
      ("unitSizeX" | uint16L) ::
      ("unitSizeY" | uint16L) ::
      ("boxSizeX" | uint16L) ::
      ("boxSizeY" | uint16L) ::
      ("attackRange" | uint8) ::
      ("reactRangeComputer" | uint8) ::
      ("reactRangeHuman" | uint8) ::
      ("armor" | uint8) ::
      ("selectableViaRectangle" | uint8) ::
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
    110 * 2,
    110 * 2,
    110 * 2,
    110 * 2,
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

  def _last(v: Vector[Int]): Int = if (v.isEmpty) 0 else v.last
  val dataSliceSizesAccu = dataSliceSizes.foldLeft(Vector[Int]())((v, _val) => v :+ (_val + _last(v)))

  def encode(a: IndexedSeq[PudUnitCharacteristic]): \/[scodec.Error, BitVector] = ???

  def decode(bits: BitVector): \/[scodec.Error, (BitVector, IndexedSeq[PudUnitCharacteristic])] = {
    // drop obsolete data
    val bytes: Array[Byte] = bits.toByteArray.drop(110 * 2 + 508 * 2)

    val hlists: IndexedSeq[Vector[Byte]] =
      for (i <- 0 until 110) yield {
        dataSliceSizes.zip(dataSliceSizesAccu).foldLeft[Vector[Byte]](Vector[Byte]())((v: Vector[Byte], p: (Int, Int)) =>
          if (p._1 % 110 == 0) {
            val blockSize = p._1 / 110
            val startOffset = p._2 - p._1

            v ++ bytes.slice(startOffset + i, startOffset + i + blockSize)
          } else {
            if (i < p._1) v ++ bytes.slice(p._2 - p._1 + i, p._2 - p._1 + i + 1)
            else v :+ 0.asInstanceOf[Byte]
          }
        )
      }

    // shapeless HList don't support tuples - damn thing

    new IndexedSeqCodec(unitCharacteristicCodec).decode(BitVector(hlists.foldLeft(Array[Byte]())(_ ++ _))) map { t =>
      (t._1, t._2 map { hl =>
        val params = hl.toArray

        val sightRange: Long = params(0).asInstanceOf[Long]
        val hitPoints: Int = params(1).asInstanceOf[Int]
        val isMagic: Boolean = params(2).asInstanceOf[Int] == 1
        val buildTime: Int = params(3).asInstanceOf[Int]
        val goldCost: Int = params(4).asInstanceOf[Int] * 10
        val lumberCost: Int = params(5).asInstanceOf[Int] * 10
        val oilCost: Int = params(6).asInstanceOf[Int] * 10
        val unitSize: (Int, Int) = (params(7).asInstanceOf[Int], params(8).asInstanceOf[Int])
        val boxSize: (Int, Int) = (params(9).asInstanceOf[Int], params(10).asInstanceOf[Int])
        val attackRange: Int = params(11).asInstanceOf[Int]
        val reactionRangeComputer: Int = params(12).asInstanceOf[Int]
        val reactionRangeHuman: Int = params(13).asInstanceOf[Int]
        val armor: Int = params(14).asInstanceOf[Int]
        val selectableViaRectangle: Boolean = params(15).asInstanceOf[Int] == 1
        val priority: Int = params(16).asInstanceOf[Int]
        val basicDamage: Int = params(17).asInstanceOf[Int]
        val piercingDamage: Int = params(18).asInstanceOf[Int]
        val weaponsUpgradable: Boolean = params(19).asInstanceOf[Int] == 1
        val armorUpgradable: Boolean = params(20).asInstanceOf[Int] == 1
        val missileWeapon: Missile = Missile(params(21).asInstanceOf[Int])
        val kind: Kind = Kind(params(22).asInstanceOf[Int])
        val decayRate: Int = params(23).asInstanceOf[Int]
        val annoyFactor: Int = params(24).asInstanceOf[Int]
        val secondMouseBtnAction: Option[MouseBtnAction] = MouseBtnAction(params(25).asInstanceOf[Int])
        val pointsForKilling: Int = params(26).asInstanceOf[Int]
        val canTarget = new CanTarget(params(27).asInstanceOf[Int])
        val flags: Long = params(28).asInstanceOf[Long]

        new PudUnitCharacteristic(
          new BasicParams(sightRange, hitPoints, armor, unitSize, priority, pointsForKilling, armorUpgradable),
          new AttackParams(basicDamage, piercingDamage, weaponsUpgradable, canTarget, attackRange),
          new UiParams(selectableViaRectangle, boxSize, missileWeapon, secondMouseBtnAction),
          new BuildParams(buildTime, goldCost, lumberCost, oilCost)
        )
      })
    }
  }
}
