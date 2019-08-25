package components

import engine.{Component, ComponentKey}

case class Player() extends Component

case object Player extends ComponentKey[Player]