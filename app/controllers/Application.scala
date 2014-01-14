package controllers

import _root_.format.pud.Pud
import play.api.mvc._
import core._
import play.api.data._
import play.api.libs.json._
import play.api.data.Forms._
import java.io.File
import se.radley.plugin.enumeration.form._
import models.UnitFeatures
import game.GameActor

object Application extends Controller {

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
        Pud(value.mapFileName).fold(
          e => NotFound(e),
          pud => Ok(views.html.game(new UnitFeatures)(value)(pud))
        )
      }
    )
  }

  def ws(settings: SinglePlayerSetting) = WebSocket.async[JsValue] { request =>
    GameActor.startGame(settings)
  }
}