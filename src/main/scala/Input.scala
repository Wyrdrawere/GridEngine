sealed trait Input

object Input {
  type Controller = PartialFunction[Input, Unit]

  final case class CursorPosition(pos: Vector2) extends Input
  final case class MouseClicked(button: InputMouseButton, pos: Vector2) extends Input
  final case class MouseHeld(button: InputMouseButton, pos: Vector2) extends Input
  final case class MouseReleased(button: InputMouseButton, pos: Vector2) extends Input
  final case class KeyPressed(key: InputKey) extends Input
  final case class KeyHeld(key: InputKey) extends Input
  final case class KeyReleased(key: InputKey) extends Input
}
