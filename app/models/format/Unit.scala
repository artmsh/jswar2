package models.format

//class Pud(
//    d : String,
//    val players : Array[PlayerType.Value] = new Array(16),
//    val races : Array[Race] = new Array(16),
//    val tileset : Tileset,
//    val mapSizeX : Short,
//    val mapSizeY : Short,
//    val tiles : Array[Tile],
//    val units : Array[Unit],
//    val aiType : Array[AiType.Value] = new Array(16),
//    val startGold : Array[Short] = new Array(16),
//    val startLumber : Array[Short] = new Array(16),
//    val startOil : Array[Short] = new Array(16),
//    val unitData: Option[UnitData],
//    val upgradeData: Option[UpgradeData],
//    val movementMap: Array[Block],
//    val actionMap: Array[ActionBlock]
//) {
//  val description = d.trim
//
//  def numPlayers(): Int = players.count(p => p != PlayerType.PlayerNobody && p != PlayerType.PlayerNeutral)
//
//
//  implicit def byteWrites = new Writes[Byte] {
//    def writes(o: Byte): JsValue = JsNumber(o)
//  }
//
//  implicit val unitWrites = Json.writes[Unit]
//
//  implicit def tilesWrites = new Writes[Tile] {
//    def writes(o: Tile): JsValue = toJson(o.tile)
//  }
//
//  implicit def blockWrites = new Writes[Block] {
//    def writes(b: Block): JsValue = toJson(b.s)
//  }
//
//  implicit def actionBlockWrites = new Writes[ActionBlock] {
//    def writes(o: ActionBlock): JsValue = toJson(o.s)
//  }
//
////  implicit def unitDataWrites = new Writes[UnitData] {
////    def writes(o: UnitData): JsValue = PudWrites.reflectionWrites.writes(o)
////  }
//
//  def asJson: JsValue = Json.obj(
//      "description" -> description,
//      "players" -> players.map(_.id),
//      "races" -> toJson(races map ( rt => toJson(rt.id) )),
//      "tileset" -> toJson(tileset.toString),
//      "mapSizeX" -> toJson(mapSizeX),
//      "mapSizeY" -> toJson(mapSizeY),
//      "tiles" -> tiles,
//      "units" -> units,
//      "aiType" -> toJson(aiType map ( b => toJson(b.id) )),
//      "startGold" -> startGold,
//      "startLumber" -> startLumber,
//      "startOil" -> startOil
////      "movementMap" -> movementMap,
////      "actionMap" -> actionMap,
////      "unitData" -> unitData,
////      "upgradeData" -> upgradeData
//    )
//}
//
////object PudWrites {
////  def reflectionWrites = new Writes[Any] {
////    def writes(o: Any): JsValue =
////      JsObject(for (field <- o.getClass.getFields) yield (field.getName, toJson(field.get(o))))
////  }
////}
//
//class UpgradeData(val isDefaultData: Boolean, val upgradeParams: Array[UpgradeParam]) {
//
//}
//
//object UpgradeData {
//  def valueOf(b: Array[Byte]): UpgradeData = {
//    val upgradeTime = b.slice(1, 1 + 52)
//    val goldCost = b.slice(1 + 52, 1 + 3 * 52).sliding(2).toArray map makeShort
//    val lumberCost = b.slice(1 + 3 * 52, 1 + 5 * 52).sliding(2).toArray map makeShort
//    val oilCost = b.slice(1 + 5 * 52, 1 + 7 * 52).sliding(2).toArray map makeShort
//    val upgradeIcon = b.slice(1 + 7 * 52, 1 + 9 * 52).sliding(2).toArray map makeShort
//    val groupAppliesTo = b.slice(1 + 9 * 52, 1 + 11 * 52).sliding(2).toArray map makeShort
//    val unknown = b.slice(1 + 11 * 52, 1 + 15 * 52).sliding(4).toArray map makeInt
//
//    val params =
//      for (i <- 0 until 52)
//      yield new UpgradeParam(upgradeTime(i), goldCost(i), lumberCost(i), oilCost(i), upgradeIcon(i), groupAppliesTo(i),
//                              unknown(i))
//
//    new UpgradeData(makeBoolean(b(0)), params.toArray)
//  }
//}
//
//class UpgradeParam(val upgradeTime: Byte, val goldCost: Short, val lumberCost: Short, val oilCost: Short,
//                    val upgradeIcon: Short, val groupAppliesTo: Short, val unknown: Int)
//
//class UnitData(val isDefaultData: Boolean, val unitParams: Array[UnitParam],
//                val terrainIndependentGraphics: Array[Short], val summerGraphics: Array[Short], val winterGraphics: Array[Short],
//                val wastelandGraphics: Array[Short]) {
//}
//
//object UnitData {
//  def valueOf(b: Array[Byte]): UnitData = {
//    val	firstConstructionFrame = b.slice(2, 2 + 110)
//    val	secondConstructionFrame = b.slice(2 + 110, 2 + 110 * 2)
//    val offset = 2 + 110 * 2 + 127 * 4 * 2
//    val	sightRange: Array[Int] = b.slice(offset, offset + 110 * 4).sliding(4).toArray map makeInt
//    val	hitPoints = b.slice(offset + 110 * 4, offset + 110 * 6).sliding(2).toArray map makeShort
//    val	magic = b.slice(offset + 110 * 6, offset + 110 * 7) map makeBoolean
//    val	buildTime = b.slice(offset + 110 * 7, offset + 110 * 8)
//    val	tenthGoldCost = b.slice(offset + 110 * 8, offset + 110 * 9)
//    val	tenthLumberCost = b.slice(offset + 110 * 9, offset + 110 * 10)
//    val	tenthOilCost = b.slice(offset + 110 * 10, offset + 110 * 11)
//    val	unitSize = b.slice(offset + 110 * 11, offset + 110 * 15).sliding(4).toArray map { ba => (makeShort(ba.slice(0, 2)), makeShort(ba.slice(2, 4))) }
//    val	boxSize = b.slice(offset + 110 * 15, offset + 110 * 19).sliding(4).toArray map { ba => (makeShort(ba.slice(0, 2)), makeShort(ba.slice(2, 4))) }
//    val	attackRange = b.slice(offset + 110 * 19, offset + 110 * 20)
//    val	reactionRange = b.slice(offset + 110 * 20, offset + 110 * 21)
//    val	unknown1 = b.slice(offset + 110 * 21, offset + 110 * 22)
//    val	armor = b.slice(offset + 110 * 22, offset + 110 * 23)
//    val	selectableViaRectangle = b.slice(offset + 110 * 23, offset + 110 * 24) map makeBoolean
//    val	priority = b.slice(offset + 110 * 24, offset + 110 * 25)
//    val	basicDamage = b.slice(offset + 110 * 25, offset + 110 * 26)
//    val	piercingDamage = b.slice(offset + 110 * 26, offset + 110 * 27)
//    val	weaponsUpgradable = b.slice(offset + 110 * 27, offset + 110 * 28) map makeBoolean
//    val	armorUpgradable = b.slice(offset + 110 * 28, offset + 110 * 29) map makeBoolean
//    val	missileWeapon = b.slice(offset + 110 * 29, offset + 110 * 30) map { Missile(_) }
//    val	unitKind = b.slice(offset + 110 * 31, offset + 110 * 32) map { UnitKind(_) }
//    val	decayRate = b.slice(offset + 110 * 32, offset + 110 * 33)
//    val	unknown2 = b.slice(offset + 110 * 33, offset + 110 * 34)
//    val role = b.slice(offset + 110 * 34, offset + 110 * 34 + 58)
//    val	pointValue = b.slice(offset + 110 * 34 + 58, offset + 110 * 36 + 58).sliding(2).toArray map makeShort
//    val canTarget = b.slice(offset + 110 * 36 + 58, offset + 110 * 37 + 58) map { new CanTarget(_) }
//    val	flags = b.slice(offset + 110 * 37 + 58, offset + 110 * 41 + 58).sliding(4).toArray map makeInt
//
//    val params: Array[UnitParam] =
//      (for {
//        i <- 0 until 110
//      } yield new UnitParam(firstConstructionFrame(i), secondConstructionFrame(i), sightRange(i), hitPoints(i),
//                            magic(i), buildTime(i), tenthGoldCost(i), tenthLumberCost(i), tenthOilCost(i),
//                            unitSize(i), boxSize(i), attackRange(i), reactionRange(i),
//                            unknown1(i), armor(i), selectableViaRectangle(i), priority(i), basicDamage(i),
//                            piercingDamage(i), weaponsUpgradable(i), armorUpgradable(i), missileWeapon(i),
//                            unitKind(i), decayRate(i), unknown2(i), if (i < 58) Some(role(i)) else None, pointValue(i),
//                            canTarget(i), flags(i))).toArray
//
//    new UnitData(if (makeShort(b.slice(0, 2)) == 0) false else true, params,
//      b.slice(2 + 110 * 2, 2 + 110 * 2 + 127 * 2).sliding(2).toArray map makeShort,
//      b.slice(2 + 110 * 2 + 127 * 2, 2 + 110 * 2 + 127 * 4).sliding(2).toArray map makeShort,
//      b.slice(2 + 110 * 2 + 127 * 4, 2 + 110 * 2 + 127 * 6).sliding(2).toArray map makeShort,
//      b.slice(2 + 110 * 2 + 127 * 6, 2 + 110 * 2 + 127 * 8).sliding(2).toArray map makeShort)
//  }
//}
//
//class UnitParam(val firstConstructionFrame: Byte, val secondConstructionFrame: Byte, val sightRange: Int, val hitPoints: Short,
//                val isMagic: Boolean, val buildTime: Byte, val tenthGoldCost: Byte, val tenthLumberCost: Byte, val tenthOilCost: Byte,
//                val unitSize: (Short, Short), val boxSize: (Short, Short), val attackRange: Byte, val reactionRange: Byte,
//                val unknown1: Byte, val armor: Byte, val selectableViaRectangle: Boolean, val priority: Byte, val basicDamage: Byte,
//                val piercingDamage: Byte, val weaponsUpgradable: Boolean, val armorUpgradable: Boolean, val missileWeapon: Missile.Value,
//                val kind: UnitKind.Value, val decayRate: Byte, val unknown2: Byte, val role: Option[Byte], val pointsForKilling: Short,
//                val canTarget: CanTarget, val flags: Int) {
//
//}
//
class CanTarget(val b: Int) extends AnyVal {
  def land = (b & 1) == 1
  def sea = (b & 2) == 2
  def air = (b & 4) == 4
}

case class Unit(x: Int, y: Int, Type: Int, player: Int, data: Int) {
  def isStartLocation = Type == 0x5e || Type == 0x5f
}
