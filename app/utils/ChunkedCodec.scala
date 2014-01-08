package utils

import scodec._
import scala.Error
import scala.Some
import scalaz.{-\/, \/-, \/}

class ChunkedCodec[A](codec: Codec[A], offset: Int) extends Codec[IndexedSeq[A]] {
  def encode(ixSeq: IndexedSeq[A]) = {
    ixSeq.traverseU { v => codec.encode(v) }.map { _.concatenate }
  }

  def decode(buffer: BitVector): Error \/ (BitVector, IndexedSeq[A]) = {
    val bldr = Vector.newBuilder[A]
    var remaining = buffer
    var error: Option[Error] = None
    while (remaining.nonEmpty) {
      codec.decode(remaining) match {
        case \/-((rest, value)) =>
          bldr += value
          remaining = rest
        case -\/(err) =>
          error = Some(err)
          remaining = BitVector.empty
      }
    }
    error.toLeftDisjunction((BitVector.empty, bldr.result))
  }

}
