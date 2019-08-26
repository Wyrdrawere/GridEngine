package components

import drawables.Color
import engine.{Component, ComponentKey}

case class BackgroundColor(color: Color) extends Component

case object BackgroundColor extends ComponentKey[BackgroundColor]
