package components

import engine.{Component, ComponentKey}
import util.Vector2

case class Scroll(progress: Int, max: Int, direction: Vector2) extends Component

case object Scroll extends ComponentKey[Scroll]