package models.format

case class Unit(x: Int, y: Int, Type: Int, player: Int, data: Int) {
  def isStartLocation = Type == 0x5e || Type == 0x5f
}
