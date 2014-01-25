package models.action

import format.pud.Tile

class MapSelector(enclosing: Option[MapSelector]) extends (() => Seq[Tile]) {

  def passable: MapSelector = new MapSelector(Some(this)) {
    override def apply() = super.apply() filter { _.isPassable }
  }

  def water: MapSelector = new MapSelector(Some(this)) {
    override def apply() = super.apply() filter { _.isWater }
  }

  def apply(): Seq[Tile] = enclosing match {
    case Some(selector) => selector()
    case None => 0 to 0x9D0 map { i => new Tile(i) }
  }
}