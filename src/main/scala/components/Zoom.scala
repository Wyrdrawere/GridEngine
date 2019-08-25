package components

import engine.{Component, ComponentKey}

case class Zoom(value: Int) extends Component

case object Zoom extends ComponentKey[Zoom]
