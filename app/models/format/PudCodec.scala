package models.format

import java.nio.charset.Charset
import shapeless.Iso
import core.{Race, Tileset, Tile}
import core.Race._
import scodec.Codecs._
import scodec.{BitVector, Codec}
import scala.collection.immutable._
import utils.SimpleIso
import java.io.FileInputStream
import scala.collection.immutable.IndexedSeq
import scalaz.\/

// According to http://cade.datamax.bg/war2x/pudspec.html
object PudCodec {
  implicit val charset = Charset.defaultCharset()

  case class SectionHeader(name: String, length: Long)
  implicit val sectionHeaderIso = Iso.hlist(SectionHeader.apply _, SectionHeader.unapply _)

  case class TypeSection(war2map: String, unused: Int, idTag: Long)
  implicit val typeSectionIso = Iso.hlist(TypeSection.apply _, TypeSection.unapply _)

  case class VerSection(version: Int)
  implicit val verSectionIso = new SimpleIso[Int, VerSection](VerSection.apply _, VerSection.unapply _).reverse

  case class DescSection(description: String)
  implicit val descSectionIso = new SimpleIso(DescSection.apply _, DescSection.unapply _).reverse

  case class OwnrSection(playerSlots: IndexedSeq[PlayerType.Value], unusableSlots: IndexedSeq[Int], neutral: Int)
  implicit val ownrSectionIso = Iso.hlist(OwnrSection.apply _, OwnrSection.unapply _)

  case class EraSection(terrain: Int)
  implicit val eraSectionIso = new SimpleIso(EraSection.apply _, EraSection.unapply _).reverse

  case class DimSection(x: Int, y: Int)
  implicit val dimSectionIso = Iso.hlist(DimSection.apply _, DimSection.unapply _)

  case class UnitDataSection1(isDefaultData: Int, overlapFrames: IndexedSeq[Int], tilesetFrames: scala.Unit,
      sightRange: IndexedSeq[Long], hitPoints: IndexedSeq[Int], isMagic: IndexedSeq[Boolean],
      buildTime: IndexedSeq[Int], tenthGoldCost: IndexedSeq[Int], tenthLumberCost: IndexedSeq[Int],
      tenthOilCost: IndexedSeq[Int], unitSize: IndexedSeq[(Int, Int)], boxSize: IndexedSeq[(Int, Int)],
      attackRange: IndexedSeq[Int], reactionRangeComputer: IndexedSeq[Int], reactionRangeHuman: IndexedSeq[Int],
      armor: IndexedSeq[Int], selectableViaRectangle: IndexedSeq[Boolean], priority: IndexedSeq[Int])
  implicit val unitDataSection1Iso = Iso.hlist(UnitDataSection1.apply _, UnitDataSection1.unapply _)

  case class UnitDataSection2(basicDamage: IndexedSeq[Int], piercingDamage: IndexedSeq[Int],
      weaponsUpgradable: IndexedSeq[Boolean], armorUpgradable: IndexedSeq[Boolean],
      missileWeapon: IndexedSeq[Missile.Value], kind: IndexedSeq[UnitKind.Value], decayRate: IndexedSeq[Int],
      annoyFactor: IndexedSeq[Int], secondMouseBtnAction: IndexedSeq[MouseBtnAction.Value],
      pointsForKilling: IndexedSeq[Int], canTarget: IndexedSeq[CanTarget], flags: IndexedSeq[Long],
      swampTilesetFrames: scala.Unit)
  implicit val unitDataSection2Iso = Iso.hlist(UnitDataSection2.apply _, UnitDataSection2.unapply _)

  case class AlowSection(allowedUnits: IndexedSeq[Long], spellYouStartWith: IndexedSeq[Long],
         allowedSpells: IndexedSeq[Long], currentResearchingSpells: IndexedSeq[Long], upgradeAllowed: IndexedSeq[Long],
         currentGivenUpgrades: IndexedSeq[Long])
  implicit val alowSectionIso = Iso.hlist(AlowSection.apply _, AlowSection.unapply _)

  case class UpgradeDataSection(isDefaultData: Int, upgradeTime: IndexedSeq[Int], goldCost: IndexedSeq[Int],
         lumberCost: IndexedSeq[Int], oilCost: IndexedSeq[Int], upgradeIcon: IndexedSeq[Int],
         groupAppliesTo: IndexedSeq[Int], affectFlags: IndexedSeq[Long])
  implicit val upgradeDataSectionIso = Iso.hlist(UpgradeDataSection.apply _, UpgradeDataSection.unapply _)

  case class SideSection(playerSlots: IndexedSeq[Race.Race], unusableSlots: IndexedSeq[Int], neutral: Int)
  implicit val sideSectionIso = Iso.hlist(SideSection.apply _, SideSection.unapply _)

  case class StartingGoldSection(gold: IndexedSeq[Int], unusableGold: IndexedSeq[Int], neutralGold: Int)
  implicit val startingGoldSectionIso = Iso.hlist(StartingGoldSection.apply _, StartingGoldSection.unapply _)

  case class StartingLumberSection(lumber: IndexedSeq[Int], unusableLumber: IndexedSeq[Int], neutralLumber: Int)
  implicit val startingLumberSectionIso = Iso.hlist(StartingLumberSection.apply _, StartingLumberSection.unapply _)

  case class StartingOilSection(oil: IndexedSeq[Int], unusableOil: IndexedSeq[Int], neutralOil: Int)
  implicit val startingOilSectionIso = Iso.hlist(StartingOilSection.apply _, StartingOilSection.unapply _)

  case class AiSection(aiType: IndexedSeq[Int], unusableAi: IndexedSeq[Int], neutralAi: Int)
  implicit val aiSectionIso = Iso.hlist(AiSection.apply _, AiSection.unapply _)

  case class TileMapSection(tiles: IndexedSeq[Int])
  implicit val tileMapSectionIso = new SimpleIso(TileMapSection.apply _, TileMapSection.unapply _).reverse

  case class MovementMapSection(surface: IndexedSeq[Int])
  implicit val movementMapSectionIso = new SimpleIso(MovementMapSection.apply _, MovementMapSection.unapply _).reverse

  case class OilMapSection(concentrationLevels: IndexedSeq[Int])
  implicit val oilMapSectionIso = new SimpleIso(OilMapSection.apply _, OilMapSection.unapply _).reverse

  case class ActionMapSection(actions: IndexedSeq[Int])
  implicit val actionMapSectionIso = new SimpleIso(ActionMapSection.apply _, ActionMapSection.unapply _).reverse

  case class UnitSection(units: IndexedSeq[Unit])
  implicit val unitIso = Iso.hlist(Unit.apply _, Unit.unapply _)
  implicit val unitSectionIso = new SimpleIso(UnitSection.apply _, UnitSection.unapply _).reverse

  case class Pud(
        _type: (SectionHeader, TypeSection),
        ver:   (SectionHeader, VerSection),
        desc:  (SectionHeader, DescSection),
        ownr:  (SectionHeader, OwnrSection),
        era:   (SectionHeader, EraSection),
        dim:   (SectionHeader, DimSection),
        udta:  (SectionHeader, (UnitDataSection1, UnitDataSection2)),
        alow:  Option[(SectionHeader, AlowSection)],
        ugrd:  (SectionHeader, UpgradeDataSection),
        side:  (SectionHeader, SideSection),
        sgld:  (SectionHeader, StartingGoldSection),
        slbr:  (SectionHeader, StartingLumberSection),
        soil:  (SectionHeader, StartingOilSection),
        aipl:  (SectionHeader, AiSection),
        mtxm:  (SectionHeader, TileMapSection),
        sqm:   (SectionHeader, MovementMapSection),
        oilm:  (SectionHeader, OilMapSection),
        regm:  (SectionHeader, ActionMapSection),
        unit:  (SectionHeader, UnitSection)
    ) {
    val mapSizeX = dim._2.x
    val mapSizeY = dim._2.y
    val tileset = Tileset(era._2.terrain + 2) // adjustment for random values
    val players = ownr._2.playerSlots.filter(p => p != PlayerType.NOBODY && p != PlayerType.NEUTRAL)
    val numPlayers = ownr._2.playerSlots.count(p => p != PlayerType.NOBODY && p != PlayerType.NEUTRAL)
    val tiles: Array[Tile] = Array()
    val aiType: Array[AiType.AiType] = Array()
  }
  implicit val pudIso = Iso.hlist(Pud.apply _, Pud.unapply _)


  implicit val sectionHeader = {
    ( "name"      | fixedSizeBytes(4, string(charset)) ) ::
    ( "length"    | uint32L )
  }.as[SectionHeader]

  implicit val typeSection = {
    ( "war2map"   | fixedSizeBytes(10, string(charset)) ) ::
    ( "unused"    | uint16L ) ::
    ( "idTag"     | uint32L )
  }.as[TypeSection]

  implicit val verSection = ("version" | uint16L).as[VerSection]
  implicit val descSection = ("description" | fixedSizeBytes(32, string(charset))).as[DescSection]
  implicit val ownrSection = {
    fixedSizeBytes(8, repeated(uint8.xmap[PlayerType.Value](PlayerType.valueOf, _.id))) ::
    fixedSizeBytes(7, repeated(uint8)).asInstanceOf[Codec[IndexedSeq[Int]]] ::
    uint8
  }.as[OwnrSection]

  implicit val eraSection = ("terrain" | uint16L).as[EraSection]

  implicit val dimSection = {
    ("x" | uint16L) :: ("y" | uint16L)
  }.as[DimSection]

  def fixedBytes(size: Int) = fixedSizeBytes(size, repeated(uint8)).asInstanceOf[Codec[IndexedSeq[Int]]]
  def fixedWords(size: Int) = fixedSizeBytes(size * 2, repeated(uint16L)).asInstanceOf[Codec[IndexedSeq[Int]]]
  def fixedLongs(size: Int) = fixedSizeBytes(size * 4, repeated(uint32L)).asInstanceOf[Codec[IndexedSeq[Long]]]

  val byte110Codec = fixedBytes(110)
  val boolean110Codec = fixedSizeBytes(110, repeated(bool)).asInstanceOf[Codec[IndexedSeq[Boolean]]]
  val word110Codec = fixedWords(110)
  val long110Codec = fixedLongs(110)

  implicit val unitDataSection1 = {
    ("default data" | uint16L) ::
    ("overlapFrames" | word110Codec) ::
    ("tilesetFrames" | ignore(8 * 508 * 2)) ::
    ("sightRange" | long110Codec) ::
    ("hitPoints" | word110Codec) ::
    ("magic" | boolean110Codec) ::
    ("buildTime" | byte110Codec) ::
    ("tenthGoldCost" | byte110Codec) ::
    ("tenthLumberCost" | byte110Codec) ::
    ("tenthOilCost" | byte110Codec) ::
    ("unitSize" | fixedSizeBytes(110 * 4, repeated(asTuple(uint16L, uint16L))).asInstanceOf[Codec[IndexedSeq[(Int, Int)]]]) ::
    ("boxSize" | fixedSizeBytes(110 * 4, repeated(asTuple(uint16L, uint16L))).asInstanceOf[Codec[IndexedSeq[(Int, Int)]]]) ::
    ("attackRange" | byte110Codec) ::
    ("reactRangeComputer" | byte110Codec) ::
    ("reactRangeHuman" | byte110Codec) ::
    ("armor" | byte110Codec) ::
    ("selectableViaRectangle" | boolean110Codec) ::
    ("priority" | byte110Codec)
  }.as[UnitDataSection1]

  implicit val unitDataSection2 = {
    ("basicDamage" | byte110Codec) ::
    ("piercingDamage" | byte110Codec) ::
    ("weaponsUpgradable" | boolean110Codec) ::
    ("armorUpgradable" | boolean110Codec) ::
    ("missileWeapon" | fixedSizeBytes(110, repeated(uint8.xmap[Missile.Value](Missile(_), _.id)))) ::
    ("unitKind" | fixedSizeBytes(110, repeated(uint8.xmap[UnitKind.Value](UnitKind(_), _.id)))) ::
    ("decayRate" | byte110Codec) ::
    ("annoyComputer" | byte110Codec) ::
    ("2ndmouseButtonAction" | fixedSizeBytes(58, repeated(int8.xmap[MouseBtnAction.Value](MouseBtnAction(_), _.id)))) ::
    ("pointForKilling" | word110Codec) ::
    ("canTarget" | fixedSizeBytes(110, repeated(uint8.xmap[CanTarget](new CanTarget(_), _.b)))) ::
    ("flags" | long110Codec) ::
    ("swampTilesetFrames" | ignore(0)) // should be conditional
  }.as[UnitDataSection2]

  implicit val pudRestrictionsSection = {
    ("units allowed" | fixedSizeBytes(16 * 4, repeated(uint32L))) ::
    ("spells you start with" | fixedSizeBytes(16 * 4, repeated(uint32L))) ::
    ("spells allowed" | fixedSizeBytes(16 * 4, repeated(uint32L))) ::
    ("spells researching" | fixedSizeBytes(16 * 4, repeated(uint32L))) ::
    ("upgrades allowed" | fixedSizeBytes(16 * 4, repeated(uint32L))) ::
    ("upgrades given" | fixedSizeBytes(16 * 4, repeated(uint32L)))
  }.as[AlowSection]

  implicit val upgradeDataSection = {
    ("defaultData" | uint16L) ::
    ("upgradeTime" | fixedBytes(52)) ::
    ("goldCost" | fixedWords(52)) ::
    ("lumberCost" | fixedWords(52)) ::
    ("oilCost" | fixedWords(52)) ::
    ("upgradeIcon" | fixedWords(52)) ::
    ("group" | fixedWords(52)) ::
    ("flags" | fixedLongs(52))
  }.as[UpgradeDataSection]

  implicit val sideSection = {
    ("playerRaces" | fixedSizeBytes(8, repeated(uint8.xmap[Race.Race](r => Race(r + 2), _.id - 2)))) ::
    ("unusableSlots" | fixedBytes(7)) ::
    ("neutralRace" | uint8)
  }.as[SideSection]

  implicit val startingGoldSection = {
    ("gold" | fixedBytes(8)) ::
    ("unusableGold" | fixedBytes(7)) ::
    ("neutralGold" | uint8)
  }.as[StartingGoldSection]

  implicit val startingLumberSection = {
    ("lumber" | fixedBytes(8)) ::
    ("unusableLumber" | fixedBytes(7)) ::
    ("neutralLumber" | uint8)
  }.as[StartingLumberSection]

  implicit val startingOilSection = {
    ("oil" | fixedBytes(8)) ::
    ("unusableOil" | fixedBytes(7)) ::
    ("neutralOil" | uint8)
  }.as[StartingOilSection]

  implicit val aiSection = {
    ("aiType" | fixedBytes(8)) ::
    ("unusableSlots" | fixedBytes(7)) ::
    ("neutralAi" | uint8)
  }.as[AiSection]

  implicit val tileMapSection = {
    repeated(uint16L)
  }.as[TileMapSection]

  implicit val movementMapSection = {
    repeated(uint16L)
  }.as[MovementMapSection]

  implicit val oilMapSection = {
    repeated(uint8)
  }.as[OilMapSection]

  implicit val actionMapSection = {
    repeated(uint16L)
  }.as[ActionMapSection]

  implicit val unitSection = {
    repeated((uint16L :: uint16L :: uint8 :: uint8 :: uint16L).as[Unit])
  }.as[UnitSection]

  implicit val pud = {
    sectionCodec(typeSection) ::
    sectionCodec(verSection) ::
    sectionCodec(descSection) ::
    sectionCodec(ownrSection) ::
    sectionCodec(eraSection) ::
    sectionCodec(dimSection) ::
    sectionCodec(asTuple(unitDataSection1, unitDataSection2)) ::
    conditional(false, sectionCodec(pudRestrictionsSection)) ::
    sectionCodec(upgradeDataSection) ::
    sectionCodec(sideSection) ::
    sectionCodec(startingGoldSection) ::
    sectionCodec(startingLumberSection) ::
    sectionCodec(startingOilSection) ::
    sectionCodec(aiSection) ::
    sectionCodec(tileMapSection) ::
    sectionCodec(movementMapSection) ::
    sectionCodec(oilMapSection) ::
    sectionCodec(actionMapSection) ::
    sectionCodec(unitSection)
  }.as[Pud]

  def sectionCodec[B](b: Codec[B])(implicit a: Codec[PudCodec.SectionHeader]): Codec[(PudCodec.SectionHeader, B)] = {
    a.flatZip[B](header => fixedSizeBytes(header.length.toInt, b))
  }

  def asTuple[A, B](a: Codec[A], b: Codec[B]): Codec[(A, B)] = {
    a.flatZip[B](_ => b)
  }
}

object PlayerType extends Enumeration {
  type PlayerType = Value
  val NEUTRAL, NOBODY, COMPUTER, PERSON, RESCUEPASSIVE, RESCUEACTIVE = Value

  def valueOf(n: Int): PlayerType.Value = n match {
    case 2 => PlayerType.NEUTRAL
    case 3 => PlayerType.NOBODY
    case 4 => PlayerType.COMPUTER
    case 5 => PlayerType.PERSON
    case 6 => PlayerType.RESCUEPASSIVE
    case 7 => PlayerType.RESCUEACTIVE
  }
}

object Pud {
  def apply(mapFileName: String): \/[scodec.Error, PudCodec.Pud] = {
    val inputStream = new FileInputStream(mapFileName)
    val bits = BitVector(Stream.continually(inputStream.read).takeWhile(i => i != -1).map(_.toByte).toArray)
    Codec.decode[PudCodec.Pud](bits)
  }
}