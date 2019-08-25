package engine

import util.{InputItem, Vector2}

sealed trait Event

object Event {

  case class RemoveComponent[C <: Component](entity: Entity, componentKey: ComponentKey[C]) extends Event

  case class KeyPressed(key: InputItem) extends Event
  case class KeyReleased(key: InputItem) extends Event
  case class MousePressed(button: InputItem) extends Event
  case class MouseReleased(button: InputItem) extends Event

  case class Move(entity: Entity, direction: Vector2) extends Event
}
