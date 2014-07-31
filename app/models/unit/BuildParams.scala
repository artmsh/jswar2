package models.unit

case class BuildParams(
    buildTime: Int,
    goldCost: Int,
    lumberCost: Int,
    oilCost: Int
)

object BuildParams {
  def none = BuildParams(0, 0, 0, 0)
}