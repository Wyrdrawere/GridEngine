package engine

import util.{InputItem, Vector2}

sealed trait Event
//todo: decide on wheter this should be unsealed with its own package, like the rest, or stay like this

object Event {

  case object Identity extends Event

  case class RemoveComponent[C <: Component](entity: Entity, componentKey: ComponentKey[C]) extends Event

  case class KeyPressed(key: InputItem) extends Event
  case class KeyReleased(key: InputItem) extends Event
  case class MousePressed(button: InputItem) extends Event
  case class MouseReleased(button: InputItem) extends Event

  case class Move(entity: Entity, direction: Vector2) extends Event
  case class InitMove(entity: Entity, direction: Vector2) extends Event
  case class ChangeJob(job: Int) extends Event

  case object ChangeMenu extends Event
}
