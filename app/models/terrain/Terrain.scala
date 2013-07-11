package models.terrain

import scala.math._
import models.unit.{Race, Unit}

class Terrain(tiles: IndexedSeq[Tile], val width: Int, val height: Int) {
  require(tiles.size == width * height)

  val data = Array.tabulate(height, width)((row, column) => tiles(row * width + column))

//  this(terrain: Terrain) {
//
//  }
//
//  def isSeen(unit: Unit): Boolean = {
//    for {
//      dy <- 0 until unit.height
//      dx <- 0 until unit.width
//      if (vision(unit.y + dy)(unit.x + dx) & 5) != 0
//    } return true
//
//    false
//  }

  private def getVision(units: Seq[Unit[_ <: Race]]): Array[Array[Int]] = {
    // 0000b - not seen, 0001b - seen, 0010b - visible now
    //                   0100b - "half"-seen, 1000b - "half"-visible
    val vision = Array.fill[Int](height, width)(0)
    (for {
      unit <- units
      i <- max(unit.y - unit.sightRange - unit.height, 0) to min(unit.y + unit.sightRange + unit.height, height)
      j <- max(unit.x - unit.sightRange - unit.width, 0) to min(unit.x + unit.sightRange + unit.width, width)
    } yield (i,j,unit)).foreach { p => p match { case (i,j,unit) =>
      val center = unit.centerCoords
      val dy = abs(center._2 - (i * 32 + 16)) + 16
      val dx = abs(center._1 - (j * 32 + 16)) + 16
      val radius = unit.sightRange * 32 + unit.height * 16
      if (dx * dx + dy * dy <= radius * radius) {
        vision(i)(j) = 3
      } else if ((dy - 32) * (dy - 32) + (dx - 32) * (dx - 32) < radius * radius) {
        if ((vision(i)(j) & 1) == 0) vision(i)(j) |= 4
        if ((vision(i)(j) & 2) == 0) vision(i)(j) |= 8
      }
    }}

    // fix half-seen tiles to full seen, example
    //  1 1 4      1 1 4
    //  1 4 4  =>  1 1 4
    //  1 1 4      1 1 4

    val mask = Array(
      Array(1,1,5),
      Array(1,4,4),
      Array(1,1,5))

    val rotations: List[(Int, Int) => (Int, Int)] = List(
       (x,y) => (x, y),
       (x,y) => (y, 2 - x),
       (x,y) => (2 - x, 2 - y),
       (x,y) => (2 - y, x)
    )

    def maskMatch(x: Int, y: Int, rotation: (Int, Int) => (Int, Int)): Boolean = {
      for {
        dy <- -1 to 1
        dx <- -1 to 1
        tile = vision(y + dy)(x + dx)
        rotatedIndex = rotation(dx + 1, dy + 1)
        if (tile & mask(rotatedIndex._2)(rotatedIndex._1)) == 0
      } return false

      true
    }

    for {
      i <- 1 until height - 1
      j <- 1 until width - 1
      rotation <- rotations
      if maskMatch(j, i, rotation)
    } vision(i)(j) = 3

//    vision foreach { p => p foreach { i => print(i.toHexString) }; println() }

    vision
  }
}