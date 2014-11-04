package game

import java.io.File

import akka.actor.{ActorRef, Props}
import akka.pattern.ask
import format.pud.Pud
import game.GameActor.{GameCreated, NewGame}
import play.Logger
import play.api.libs.concurrent.Akka

class Games {
  val puds: Map[String, Pud] = new File("conf/maps/multi")
    .listFiles()
    .flatMap(f => Pud(f.getAbsolutePath) map { p => f.getName -> p } ).toMap

  var games = List[(Game, ActorRef)]()

  def createNewGame(gameSettings: GameSettings, onCreate: => Unit): Unit = {
    val gameActor = Akka.system.actorOf(Props(new GameActor(
      puds(gameSettings.pudFileName), gameSettings.tileset, gameSettings.peasantOnly, gameSettings.playerSettings.toList map { ps =>
        (ps._1, ps._2.race, ps._2.control)
      })))

    (gameActor ? NewGame).foreach({
      case GameCreated(game) =>
        Logger.info(s"Created game$games.size")
        games :+ (game, gameActor)
        onCreate
    })

  }

  def getGame(gameId: Int): Option[Game] = {
    if (gameId < games.size) Some(games(gameId)._1)
    else None
  }
}
