package controllers

import play.api.mvc._
import utils.PudParser
import play.api.templates.Html
import io.Source
import core._
import util.Random

object Application extends Controller {

  var core: Core = null

  def index = Action {
    Ok(Html(Source.fromFile("public/game.html").mkString))
  }

  def war2 = Action {
    Ok(views.html.war2())
  }

  def maps(mapName: String) = Action {
    Ok(PudParser.parse(mapName).asJson)
  }

  def data(race: String, resources: String, units: String, opponents: String, tileset: String, mapname: String) = Action {
//    core match {
//      case null => {
        core = new Core(Race.withName0(race), Resources.withName0(resources), Units.withName0(units),
          Opponents.withName0(opponents), Tileset.withName0(tileset), PudParser.parse(mapname.substring(5)))
        Ok(core.getData.asJson)
//      }
//      case _ => BadRequest
//    }
  }
}