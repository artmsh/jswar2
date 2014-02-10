package format.pud

import scodec.Codec
import java.nio.charset.Charset
import shapeless.Iso
import utils.SimpleIso
import models.unit.UnitCharacteristic
import scodec.Codecs._
import controllers.Race

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

  case class OwnrSection(playerSlots: IndexedSeq[PlayerType], unusableSlots: IndexedSeq[Int], neutral: Int)
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

  case class MovementMapSection(surface: IndexedSeq[Int])
  implicit val movementMapSectionIso = new SimpleIso(MovementMapSection.apply _, MovementMapSection.unapply _).reverse

  case class OilMapSection(concentrationLevels: IndexedSeq[Int])
  implicit val oilMapSectionIso = new SimpleIso(OilMapSection.apply _, OilMapSection.unapply _).reverse

  case class ActionMapSection(actions: IndexedSeq[Int])
  implicit val actionMapSectionIso = new SimpleIso(ActionMapSection.apply _, ActionMapSection.unapply _).reverse

  case class Unit(x: Int, y: Int, Type: Int, player: Int, data: Int) {
    def isStartLocation = Type == 0x5e || Type == 0x5f
  }
  case class UnitSection(units: IndexedSeq[Unit])
  implicit val unitIso = Iso.hlist(Unit.apply _, Unit.unapply _)
  implicit val unitSectionIso = new SimpleIso(UnitSection.apply _, UnitSection.unapply _).reverse

  case class _Pud(
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
      mtxm:  (SectionHeader, scala.collection.immutable.IndexedSeq[Tile]),
      sqm:   (SectionHeader, MovementMapSection),
      oilm:  (SectionHeader, OilMapSection),
      regm:  (SectionHeader, ActionMapSection),
      unit:  (SectionHeader, UnitSection)
    )
  implicit val pudIso = Iso.hlist(_Pud.apply _, _Pud.unapply _)

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
    // todo fix
    fixedSizeBytes(8, repeated(uint8.xmap[PlayerType](PlayerType(_), pt => 0))).asInstanceOf[Codec[IndexedSeq[PlayerType]]] ::
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

  implicit val unitDataSection = {
    ("default data" | uint16L) ::
    ("unit characteristics" | fixedSizeBytes(5694, new UnitCharacteristicCodec()))
  }.as[UnitDataSection]

  implicit val pudRestrictionsSection = {
    ("units allowed" | fixedSizeBytes(16 * 4, repeated(uint32L))).asInstanceOf[Codec[IndexedSeq[Long]]] ::
    ("spells you start with" | fixedSizeBytes(16 * 4, repeated(uint32L))).asInstanceOf[Codec[IndexedSeq[Long]]] ::
    ("spells allowed" | fixedSizeBytes(16 * 4, repeated(uint32L))).asInstanceOf[Codec[IndexedSeq[Long]]] ::
    ("spells researching" | fixedSizeBytes(16 * 4, repeated(uint32L))).asInstanceOf[Codec[IndexedSeq[Long]]] ::
    ("upgrades allowed" | fixedSizeBytes(16 * 4, repeated(uint32L))).asInstanceOf[Codec[IndexedSeq[Long]]] ::
    ("upgrades given" | fixedSizeBytes(16 * 4, repeated(uint32L))).asInstanceOf[Codec[IndexedSeq[Long]]]
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
    ("playerRaces" | fixedSizeBytes(8, repeated(uint8.xmap[Race.Race](r => Race(r + 2), _.id - 2)))).asInstanceOf[Codec[IndexedSeq[Race.Race]]] ::
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

  implicit val tileMapSection = repeated[Tile](uint16L.xmap(new Tile(_), _.tile))

  implicit val movementMapSection = {
    repeated(uint16L).asInstanceOf[Codec[IndexedSeq[Int]]]
  }.as[MovementMapSection]

  implicit val oilMapSection = {
    repeated(uint8).asInstanceOf[Codec[IndexedSeq[Int]]]
  }.as[OilMapSection]

  implicit val actionMapSection = {
    repeated(uint16L).asInstanceOf[Codec[IndexedSeq[Int]]]
  }.as[ActionMapSection]

  implicit val unitSection = {
    repeated((uint16L :: uint16L :: uint8 :: uint8 :: uint16L).as[Unit]).asInstanceOf[Codec[IndexedSeq[Unit]]]
  }.as[UnitSection]

  implicit val pud = {
    sectionCodec(typeSection) ::
    sectionCodec(verSection) ::
    sectionCodec(descSection) ::
    sectionCodec(ownrSection) ::
    sectionCodec(eraSection) ::
    sectionCodec(dimSection) ::
    sectionCodec(unitDataSection) ::
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
  }.as[_Pud]

  def sectionCodec[B](b: Codec[B])(implicit a: Codec[PudCodec.SectionHeader]): Codec[(PudCodec.SectionHeader, B)] = {
    a.flatZip[B](header => fixedSizeBytes(header.length.toInt, b))
  }

  def asTuple[A, B](a: Codec[A], b: Codec[B]): Codec[(A, B)] = {
    a.flatZip[B](_ => b)
  }
}