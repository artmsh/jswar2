package utils

object ArrayUtils {
  // row, col, value
  def array2DasCoords[T](a: Array[Array[T]]): List[(Int, Int, T)] =
    (for {
      i <- 0 until a.length
      j <- 0 until a(i).length
    } yield (i, j, a(i)(j))).toList
}
