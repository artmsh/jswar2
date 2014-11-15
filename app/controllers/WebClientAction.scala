package controllers

import game.Order

sealed trait WebClientAction
case object WebSocketInitOk extends WebClientAction
case object ClientInitOk extends WebClientAction
case class ActionEvents(actionEvents: List[(Int, Order)]) extends WebClientAction