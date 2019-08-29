package components

import engine.{Component, ComponentKey, Drawable}

case class Cursor(drawable: Drawable) extends Component

case object Cursor extends ComponentKey[Cursor]