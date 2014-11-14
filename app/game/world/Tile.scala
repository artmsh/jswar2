package game.world

trait Tile {
  val number: Int
}

case class Water(number: Int = 0x10) extends Tile
case class Coast(number: Int = 0x30) extends Tile
case class Ground(number: Int = 0x50) extends Tile
case class Forest(number: Int = 0x70) extends Tile
case class Mountains(number: Int = 0x80) extends Tile
case class HumanWall(number: Int = 0x90) extends Tile
case class OrcWall(number: Int = 0xA0) extends Tile

object Tile {
  def apply(tile: format.pud.Tile): Tile = tile.tile >> 4 match {
    case 0x1 => Water(tile.tile)
    case 0x2 => Water(tile.tile)
    case 0x3 => Coast(tile.tile)
    case 0x4 => Coast(tile.tile)
    case 0x5 => Ground(tile.tile)
    case 0x6 => Ground(tile.tile)
    case 0x7 => Forest(tile.tile)
    case 0x8 => Mountains(tile.tile)
    case 0x9 => HumanWall(tile.tile)
    case 0xA => OrcWall(tile.tile)
    case 0xB => HumanWall(tile.tile)
    case 0xC => OrcWall(tile.tile)
  }
}