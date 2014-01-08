package models.format

import java.nio.charset.Charset
import shapeless.Iso
import core.{Race, Tileset}
import scodec.Codecs._
import scodec.{BitVector, Codec}
import scala.collection.immutable._
import utils.SimpleIso
import java.io.FileInputStream
import scala.collection.immutable.IndexedSeq
import scalaz.\/
import models.terrain.Tile
import models.unit.{Kind, Missile, UnitCharacteristic}

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

  case class UnitDataSection(isDefaultData: Int, unitCharacteristics: IndexedSeq[UnitCharacteristic])
  implicit val unitDataSectionIso = Iso.hlist(UnitDataSection.apply _, UnitDataSection.unapply _)

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
        udta:  (SectionHeader, UnitDataSection),
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
    val tiles = mtxm._2.tiles.map(new Tile(_))
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

  val unitCharacteristicCodec = {
    ("overlapFrames" | ignore(2)) ::
    ("tilesetFrames" | ignore(3 * 2)) ::
    ("sightRange" | uint32L) ::
    ("hitPoints" | uint16L) ::
    ("magic" | bool) ::
    ("buildTime" | uint8) ::
    ("tenthGoldCost" | uint8.xmap(_ * 10)) ::
    ("tenthLumberCost" | uint8.xmap(_ * 10)) ::
    ("tenthOilCost" | uint8.xmap(_ * 10)) ::
    ("unitSize" | asTuple(uint16L, uint16L)) ::
    ("boxSize" | asTuple(uint16L, uint16L)) ::
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
    ("missileWeapon" | uint8.xmap[Missile](Missile(_), _.id)) ::
    ("unitKind" | uint8.xmap[Kind](UnitKind(_), _.id)) ::
    ("decayRate" | uint8) ::
    ("annoyComputer" | uint8) ::
    ("2ndmouseButtonAction" | fixedSizeBytes(58, repeated(int8.xmap[MouseBtnAction.Value](MouseBtnAction(_), _.id)))) ::
    ("pointForKilling" | uint16L) ::
    ("canTarget" | uint8.xmap[CanTarget](new CanTarget(_), _.b)) ::
    ("flags" | uint32L)
  }.as[UnitCharacteristic]

  implicit val unitDataSection = {
    ("default data" | uint16L) ::
    ("unit characteristics" | chunked(unitCharacteristicCodec, 110))
  }.as[UnitDataSection]

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

//class Block(val s: Short) extends AnyVal {
//  def isLand = (s == 0x1)
//  def isCoastCorner = (s == 0x2)
//  def isDirt = (s == 0x11)
//  def isWater = (s == 0x40)
//  def isForestOrMountain = (s == 0x81)
//  def isCoast = (s == 0x82)
//  def isWall = (s == 0x8d)
//  def isBridge = (s == 0)
//  def isSpace = ((s & 0x0f00) == 0x0f00)
//  def isCave = ((s & 0x0200) == 0x0200)
//}
//
//class ActionBlock(val s: Short) extends AnyVal {
//  def isWater = (s == 0)
//  def isLand = (s == 0x4000)
//  def isIsland = (s == 0xfaff)
//  def isWall = (s == 0xfbff)
//  def isMountains = (s == 0xfdff)
//  def isForest = (s == 0xfeff)
//}