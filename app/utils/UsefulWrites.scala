package utils

import play.api.libs.json.{JsArray, JsValue, Writes}
import reflect.ClassTag

trait UsefulWrites {
  implicit def tuple2Writes[A : ClassTag, B: ClassTag](implicit fmtA: Writes[A], fmtB: Writes[B]): Writes[(A, B)] = new Writes[(A, B)] {
    def writes(o: (A, B)): JsValue = JsArray(Seq(fmtA.writes(o._1), fmtB.writes(o._2)))
  }
}
