package components

import drawables.Text
import engine.{Component, ComponentKey, Event}
import util.Vector2

case class ListMenu(items: List[(Text, Event)]) extends Component

case object ListMenu extends ComponentKey[ListMenu]
