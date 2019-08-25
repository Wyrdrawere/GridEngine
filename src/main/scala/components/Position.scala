package components

import engine.{Component, ComponentKey}
import util.Vector2

case class Position(value: Vector2) extends Component

case object Position extends ComponentKey[Position]
