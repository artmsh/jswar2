package controllers

import game.Order

trait WebClientAction
case object WsInitOk extends WebClientAction
case object ClInitOk extends WebClientAction
case class ActionEvents(actionEvents: List[(Int, Order)]) extends WebClientAction