package utils

import play.api.libs.json.Json

object Helper {
  def initialData: String = {
    Json.obj(
      "peasant" -> Json.arr("repair")
    ).toString()
  }
}
