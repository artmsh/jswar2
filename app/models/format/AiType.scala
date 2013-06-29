package models.format

//
//object ShortImplicits {
//  implicit def byteToShort(value: (Byte, Byte)): Short = (makeUnsigned(value._1) + (makeUnsigned(value._2) << 8)).toShort
//}
//
//object UnitReads extends Reads[Unit] {
//  import ShortImplicits._
////  def valueOf(b: Array[Byte]): Unit = {
////    new Unit(makeShort(b.slice(0, 2)), makeShort(b.slice(2, 4)), b(4), b(5), makeShort(b.slice(6, 8)))
////  }
//
//  def reads(in: Input): Unit = Unit((in.readByte, in.readByte), (in.readByte, in.readByte),
//    in.readByte, in.readByte, (in.readByte, in.readByte))
//}
//
object UnitKind extends Enumeration {
  val Land, Fly, Naval = Value
  val unknown1 = Value(3)
  val unknown2 = Value(100)
}

object MouseBtnAction extends Enumeration {
  val unknown = Value(0)
  val attack = Value(1)
  val move = Value(2)
  val harvest = Value(3)
  val haul_oil = Value(4)
  val demolish = Value(5)
  val sail = Value(6)
}

object Missile extends Enumeration {
  val lightning, griffon_hammer, dragon_breath, flame_shield, flame_shield_self, blizzard, death_and_decay, big_cannon,
  black_powder, heal_effect, touch_of_death, rune, tornado, catapult_rock, ballista_bolt, arrow, axe, submarine_missile,
  turtle_missile, dark_flame, bright_flame, blood, more_black_powder, explosion, small_cannon, metal_spark,
  mini_explosion, demon_fire, green_cross, none = Value
}

object AiType extends Enumeration {
  type AiType = Value

  val LandAttack = Value(0)
  val Passive = Value(1)
  val SeaAttack = Value(0x19)
  val AirAttack = Value(0x1A)
}
