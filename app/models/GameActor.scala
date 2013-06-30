package models

import akka.actor.{Props, Actor}
import akka.pattern.ask
import scala.concurrent.duration._
import play.api.libs.concurrent.Akka
import play.api.libs.json.{JsValue, JsObject, JsString}
import play.api.libs.iteratee._
import scala.concurrent.Future
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits._
import controllers.SinglePlayerSetting
import models.format.PlayerType

object GameActor {
  implicit val timeout = akka.util.Timeout(1 second)

  lazy val defaultActor = Akka.system.actorOf(Props[GameActor])

  def startGame(settings: SinglePlayerSetting): Future[(Iteratee[JsValue, _], Enumerator[JsValue])] = {
    (defaultActor ? CreateWorld(settings)).map {
      case Connected(enumerator) =>
        val iteratee = Iteratee.foreach[JsValue] { event =>
          defaultActor ! Update(event.as[ClientEvents])
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
      sender ! Connected(chatEnumerator)
      world = World(settings)
      currentPlayer = world.players.find(_._type == PlayerType.PERSON).get
      updateClient(world.diff(EmptyWorld, currentPlayer))
    }

    case Update(events: ClientEvents) => {
      val oldWorld = world
      world = world.update(events)
      updateClient(world.diff(oldWorld, currentPlayer))
    }

    case Quit() => {

    }
  }

  def updateClient(data: UpdateData) {
    chatChannel.push(data.asJson)
  }
}

case class Connected(enumerator:Enumerator[JsValue])
case class CannotConnect(msg: String)

case class CreateWorld(settings: SinglePlayerSetting)
case class Update(events: ClientEvents)
case class Quit()