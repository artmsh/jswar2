package models.format

import scodec.Codecs._
import shapeless.Iso
import java.nio.charset.Charset
import scodec.Codec
import core.{Tileset, Tile}
import core.Race._








object PlayerTypes extends Enumeration {
  type PlayerTypes = Value
  val PlayerNeutral, PlayerNobody, PlayerComputer, PlayerPerson, PlayerRescuePassive, PlayerRescueActive = Value

  def valueOf(b : Byte) : PlayerTypes.Value = b match {
    case 2 => PlayerTypes.PlayerNeutral
    case 3 => PlayerTypes.PlayerNobody
   	case 4 => PlayerTypes.PlayerComputer
    case 5 => PlayerTypes.PlayerPerson
   	case 6 => PlayerTypes.PlayerRescuePassive
    case 7 => PlayerTypes.PlayerRescueActive
  }
}
//
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