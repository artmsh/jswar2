package format.pud

class CanTarget(val b: Int) extends AnyVal {
  def land = (b & 1) == 1
  def sea = (b & 2) == 2
  def air = (b & 4) == 4
}
