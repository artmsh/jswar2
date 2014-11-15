package game.world

case class TileVisibility(x: Int, y: Int, visibility: Visibility) {
  def checkBounds(maxX: Int, maxY: Int): Boolean = {
    x >= 0 && x < maxX && y >= 0 && y < maxY
  }
}

trait Visibility extends Ordered[Visibility]
case object FullVisible extends Visibility {
  override def compare(that: Visibility): Int = if (that == FullVisible) 0 else 1
}
case object HalfVisible extends Visibility {
  override def compare(that: Visibility): Int = that match {
    case FullVisible => -1
    case HalfVisible => 0
    case NonVisible => 1
  }
}
case object NonVisible extends Visibility {
  override def compare(that: Visibility): Int = if (that == NonVisible) 0 else -1
}