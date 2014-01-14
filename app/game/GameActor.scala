package game

import akka.actor.{Props, Actor}
import scala.concurrent.duration._
import play.api.libs.concurrent.Akka
import play.api.libs.json.JsValue
import play.api.libs.iteratee._
import scala.concurrent.Future
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits._
import controllers.SinglePlayerSetting
import play.Logger
import models._
import play.api.libs.json.JsString
import play.api.libs.json.JsObject
import format.pud.Person

object GameActor {
  implicit val timeout = akka.util.Timeout(1 second)

  lazy val defaultActor = Akka.system.actorOf(Props[GameActor])

  def startGame(settings: SinglePlayerSetting): Future[(Iteratee[JsValue, _], Enumerator[JsValue])] = {
    (defaultActor ? CreateWorld(settings)).map {
      case Connected(enumerator) =>
        val iteratee = Iteratee.foreach[JsValue] { event =>
          defaultActor ! UpdateWithEvents(event.as[List[ClientEvent]])
        }.map { _ =>
          defaultActor ! Quit()
        }

        (iteratee, enumerator)

      case CannotConnect(error) =>
        // Connection error

        // A finished Iteratee sending EOF
        val iteratee = Done[JsValue, Unit]((), Input.EOF)

        // Send an error and close the socket
        val enumerator =  Enumerator[JsValue](JsObject(Seq("error" -> JsString(error)))).andThen(Enumerator.enumInput(Input.EOF))

        (iteratee, enumerator)
    }
  }
}

class GameActor extends Actor {
  var world: World = EmptyWorld
  var currentPlayer: Player = _
  val (chatEnumerator, chatChannel) = Concurrent.broadcast[JsValue]

  override def receive = {
    case CreateWorld(settings) => {
      Logger.debug("Create world event: " + settings)

      sender ! Connected(chatEnumerator)
      world = World(settings)
      currentPlayer = world.players.find(_._type == Person).get

      Akka.system.scheduler.schedule(
        1 second,
        (1f / 30) second,
        GameActor.defaultActor,
        Update
      )
//      updateClient(world.diff(EmptyWorld, currentPlayer))
    }

    case UpdateWithEvents(events: List[ClientEvent]) => {
      // todo world changes state between update calls
      val oldWorld = world
      world = world.update(events)
      updateClient(world.diff(oldWorld, currentPlayer))
    }

    case Update => {
      world = world.nextTurn()
    }

    case Quit() => {
      Logger.debug("Game over")
    }
  }

  def updateClient(data: UpdateData) {
    chatChannel.push(data.asJson)
  }
}

case class Connected(enumerator:Enumerator[JsValue])
case class CannotConnect(msg: String)

case class CreateWorld(settings: SinglePlayerSetting)
case class Update()
case class UpdateWithEvents(events: List[ClientEvent])
case class Quit()