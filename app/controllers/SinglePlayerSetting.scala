package controllers

import core._
import play.api.mvc.QueryStringBindable

case class SinglePlayerSetting(race: Race.Race, resources: Resources.Resources, units: Units.Units,
                               opponents: Opponents.Opponents, tileset: Tileset.Tileset, mapFileName: String)

object SinglePlayerSetting {
  implicit def singlePlayerSettingBinder(implicit stringBinder: QueryStringBindable[String]) =
    new QueryStringBindable[SinglePlayerSetting] {
      override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, SinglePlayerSetting]] = {
        for {
          race <- stringBinder.bind(key + ".race", params)
          resources <- stringBinder.bind(key + ".resources", params)
          units <- stringBinder.bind(key + ".units", params)
          opponents <- stringBinder.bind(key + ".opponents", params)
          tileset <- stringBinder.bind(key + ".tileset", params)
          mapFileName <- stringBinder.bind(key + ".mapFileName", params)
        } yield {
          (race, resources, units, opponents, tileset, mapFileName) match {
            case (Right(_race), Right(_resources), Right(_units), Right(_opponents), Right(_tileset), Right(_mapFileName)) =>
              Right(SinglePlayerSetting(Race.withName(_race), Resources.withName(_resources), Units.withName(_units),
                Opponents.withName(_opponents), Tileset.withName(_tileset), _mapFileName))
            case _ => Left("Unable to bind a SinglePlayerSetting")
          }
        }
      }

      override def unbind(key: String, value: SinglePlayerSetting): String = {
        stringBinder.unbind(key + ".race", value.race.toString) + "&" +
          stringBinder.unbind(key + ".resources", value.resources.toString) + "&" +
          stringBinder.unbind(key + ".units", value.units.toString) + "&" +
          stringBinder.unbind(key + ".opponents", value.opponents.toString) + "&" +
          stringBinder.unbind(key + ".tileset", value.tileset.toString) + "&" +
          stringBinder.unbind(key + ".mapFileName", value.mapFileName.toString) + "&"
      }
    }
}
