package models.terrain

class Tile(val tile: Int) extends AnyVal {
  def isWater: Boolean =
    (tile & 0xFF10) == 0x0010 || (tile & 0xFF20) == 0x0020 ||
      (tile & 0xF100) == 0x0100 || (tile & 0xF200) == 0x0200


  def isPassable: Boolean =
    (tile & 0xFF30) == 0x0030 || (tile & 0xFF40) == 0x0040 ||
      (tile & 0xFF50) == 0x0050 || (tile & 0xFF60) == 0x0060 ||
      (tile & 0xF300) == 0x0300 || (tile & 0xF500) == 0x0500 || (tile & 0xF600) == 0x0600
}