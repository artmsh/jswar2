package models.unit

class BuildParams(
    val buildTime: Int,
    val goldCost: Int,
    val lumberCost: Int,
    val oilCost: Int
)

object BuildParams {
  def none = new BuildParams(0, 0, 0, 0)
}