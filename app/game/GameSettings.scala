package game

import controllers.Tileset
import play.api.mvc.QueryStringBindable

case class GameSettings(pudFileName: String, tileset: Tileset.Value, controlledPlayerNo: Int, playerSettings: Map[Int, PlayerSettings], peasantOnly: Boolean)
case class PlayerSettings(race: Race, control: Control)

object GameSettings {
  implicit def gameSettingsBinder(implicit stringBinder: QueryStringBindable[String], intBinder: QueryStringBindable[Int]
               , booleanBinder: QueryStringBindable[Boolean]) =
    new QueryStringBindable[GameSettings] {
      def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, GameSettings]] = {
        // todo implement
        Some(Right(GameSettings("", Tileset.SUMMER, 0, Map(), false)))
      }

      def unbind(key: String, value: GameSettings): String = {
        intBinder.unbind(key + ".controlledPlayerNo", value.controlledPlayerNo) + "&" +
        booleanBinder.unbind(key + ".peasantOnly", value.peasantOnly) +
          value.playerSettings.map(p => "&" + PlayerSettings.playerSettingsQSB.unbind(key + "player" + p._1, p._2)).mkString
      }
    }
}

object PlayerSettings {
  implicit val playerSettingsQSB = new QueryStringBindable[PlayerSettings] {
    def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, PlayerSettings]] = {
      for {
        race <- QueryStringBindable.bindableString.bind(key + ".race", params)
      } yield {
        race match {
          case Right(_race) => Right(PlayerSettings(Race(_race), HumanControl))
          case _ => Left("Unable to bind a PlayerSettings")
        }
      }
    }

    def unbind(key: String, value: PlayerSettings): String =
      QueryStringBindable.bindableString.unbind(key + ".race", value.race.getClass.getSimpleName)
  }
}