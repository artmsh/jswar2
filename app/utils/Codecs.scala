package utils

object Codecs {
  def chunked[A](codec: Codec[A], offset: Int): Codec[IndexedSeq[A]] = new ChunkedCodec(codec, offset)
}
