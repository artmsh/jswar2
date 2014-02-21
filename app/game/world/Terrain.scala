package game.world

import scala.math._
import game.unit.Unit
import format.pud.Tile
import play.Logger

class Terrain(var tiles: Vector[Vector[Tile]], val width: Int, val height: Int) {
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

  def maxVision(v1: Int, v2: Int): Int = {
    if (v1 != 0 && v2 != 0) min(v1, v2)
    else max(v1, v2)
  }

  def mergeVision(vision1: Array[Array[Int]], vision2: Array[Array[Int]]): Array[Array[Int]] = {
    (vision1 zip vision2) map { p => (p._1 zip p._2) map { p => maxVision(p._1, p._2) } }
  }

  def getVision(units: Seq[Unit]): Array[Array[Int]] = {
    // 0000b - not seen, 0001b - seen, 0010b - visible now
    //                   0100b - "half"-seen, 1000b - "half"-visible
    val vision = Array.fill[Int](height, width)(0)

    val tt = System.currentTimeMillis()
    (for {
      unit <- units
      i <- max(unit.y - unit.ch.sightRange.toInt - unit.height, 0) to min(unit.y + unit.ch.sightRange.toInt + unit.height, height)
      j <- max(unit.x - unit.ch.sightRange.toInt - unit.width, 0) to min(unit.x + unit.ch.sightRange.toInt + unit.width, width)
    } yield (i,j,unit)).foreach { p => p match { case (i,j,unit) =>
      val center = unit.centerCoords
      val dy = abs(center._2 - (i * 32 + 16)) + 16
      val dx = abs(center._1 - (j * 32 + 16)) + 16
      val radius = unit.ch.sightRange * 32 + unit.height * 16
      if (dx * dx + dy * dy <= radius * radius) {
        vision(i)(j) = 3
      } else if ((dy - 32) * (dy - 32) + (dx - 32) * (dx - 32) < radius * radius) {
        if ((vision(i)(j) & 1) == 0) vision(i)(j) |= 4
        if ((vision(i)(j) & 2) == 0) vision(i)(j) |= 8
      }
    }}

    Logger.debug(s"vision for ${units.size} takes " + (System.currentTimeMillis() - tt) + " ms")

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

    val t2 = System.currentTimeMillis()

    for {
      i <- 1 until height - 1
      j <- 1 until width - 1
      rotation <- rotations
      if maskMatch(j, i, rotation)
    } vision(i)(j) = 3

    Logger.debug("maskMatch takes " + (System.currentTimeMillis() - t2) + " ms")

//    vision foreach { p => p foreach { i => print(i.toHexString) }; println() }

    vision
  }
}
