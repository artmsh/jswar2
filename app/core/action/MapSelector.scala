package core.action

import core.Tile

class MapSelector(enclosing: Option[MapSelector]) extends (() => Seq[Tile]) {

  def passable: MapSelector = new MapSelector(Some(this)) {
    override def apply() = this()
  }

  def water(): MapSelector = new MapSelector(Some(this)) {
    override def apply() = this() filter {  }
  }


  def apply(): Seq[Tile] = enclosing match {
    case Some(selector) => selector(v1)
    case None => (0 to 0x9D0) map { i => new Tile(i) }
  }
}