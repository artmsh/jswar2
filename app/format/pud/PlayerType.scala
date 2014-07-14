package format.pud

trait PlayerType extends Ordered[PlayerType] {
  val num: Int
  def compare(that: PlayerType): Int = num - that.num
}
case object Neutral extends PlayerType { val num = 15 }
case object Nobody extends PlayerType { val num = 3 }
case object Computer extends PlayerType { val num = 4 }
case object Person extends PlayerType { val num = 5 }
case object RescuePassive extends PlayerType { val num = 6 }
case object RescueActive extends PlayerType { val num = 7 }
object PlayerType {
  def apply(n: Int): PlayerType = n match {
    case 1 => Computer
    case 2 => Neutral
    case 3 => Nobody
    case 4 => Computer
    case 5 => Person
    case 6 => RescuePassive
    case 7 => RescueActive
    case 15 => Neutral
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