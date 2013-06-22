package controllers

import play.api.mvc._
import core._
import play.api.libs.iteratee.{Enumerator, Iteratee}
import play.api.data._
import play.api.data.Forms._
import java.io.{FileInputStream, File}
import se.radley.plugin.enumeration.form._
import models.{PudCodec, UnitFeatures}
import scodec.{Codec, BitVector}
import scalaz.\/
import play.api.libs.concurrent.Akka
import play.api.Play.current

object Application extends Controller {

  var core: Core = null

  def index = Action {
    Ok(views.html.newSinglePlayer(singlePlayerForm))
  }

  val singlePlayerForm = Form(
    mapping(
      "race" -> enum(Race),
      "resources" -> enum(Resources),
      "units" -> enum(Units),
      "opponents" -> enum(Opponents),
      "tileset" -> enum(Tileset),
      "mapFileName" -> nonEmptyText.verifying("Map not found", new File(_).exists())
    )(SinglePlayerSetting.apply)(SinglePlayerSetting.unapply)
  )

  def singlePlayerGame = Action { implicit request =>
    singlePlayerForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.newSinglePlayer(formWithErrors)),
      value => {
        val inputStream = new FileInputStream(value.mapFileName)
        val bits = BitVector(Stream.continually(inputStream.read).takeWhile(-1 !=).map(_.toByte).toArray)
        val pud: \/[scodec.Error, PudCodec.Pud] = Codec.decode[PudCodec.Pud](bits)
        Ok(views.html.game(new UnitFeatures)(pud.toOption.get))
      }
    )
  }

  def ws = WebSocket.async[String] { request =>

    Akka.future {
      val out = Enumerator.imperative[String]()
      val in = Iteratee.foreach[String] {
        msg =>
          out.push(msg)
      }
      (in, out)

//    val in = Iteratee.foreach[String](println).mapDone { _ =>
//      println("Disconnected")
//    }
//
//    val out = Enumerator[String](Helper.initialData)
//
//    (in, out)
  }}

  def war2 = Action {
    Ok(views.html.war2())
  }

//  def maps(mapName: String) = Action {
//    Ok(PudParser.parse(mapName).asJson)
//  }

//  def units = Action {
//    Ok(Json.toJson(Macros.unitsDescription))
//  }

//  def data(race: String, resources: String, units: String, opponents: String, tileset: String, mapname: String) = Action {
////    core match {
////      case null => {
//        core = new Core(Race.withName0(race), Resources.withName0(resources), Units.withName0(units),
//          Opponents.withName0(opponents), Tileset.withName0(tileset), PudParser.parse(mapname.substring(5)))
//        Ok(core.getData.asJson)
////      }
////      case _ => BadRequest
////    }
//  }
}

case class SinglePlayerSetting(race: Race.Race, resources: Resources.Resources, units: Units.Units,
                               opponents: Opponents.Opponents, tileset: Tileset.Tileset, mapFileName: String)