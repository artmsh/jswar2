package utils

import shapeless.Iso

class SimpleIso[T, U](val toF: T => U, val fromF: U => Option[T]) extends Iso[T, U] {
  def to(t: T): U = toF(t)
  def from(u: U): T = fromF(u).get
}
