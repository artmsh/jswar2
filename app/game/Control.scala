package game

sealed trait Control
case object HumanControl extends Control
case object NeutralAiControl extends Control
case object DefaultAiControl extends Control