package components

import engine.{Component, ComponentKey}

case class Cursor() extends Component

case object Cursor extends ComponentKey[Cursor]