trait Mutation

object Mutation {

  final case object MakeSubMenu extends Mutation
  final case class ChangeJob(job: Int) extends Mutation

  //todo: phase out above

  final case object Identity extends Mutation
  final case class SetBox(box: Statebox) extends Mutation
  final case class SetChild(state: Option[Stateful]) extends Mutation
  final case class SetChildState(state: Option[State]) extends Mutation
  final case class SetReturnMutation(mutation: Mutation) extends Mutation
  final case class SetDelay(inputDelay: InputDelay) extends Mutation
  final case class Composite(mut: List[Mutation]) extends Mutation
  final case class CursorPosition(pos: Vector2) extends Mutation
  final case class MouseClicked(button: InputMouseButton, pos: Vector2) extends Mutation
  final case class MouseHeld(button: InputMouseButton, pos: Vector2) extends Mutation
  final case class MouseReleased(button: InputMouseButton, pos: Vector2) extends Mutation
  final case class KeyPressed(key: InputKey) extends Mutation
  final case class KeyHeld(key: InputKey) extends Mutation
  final case class KeyReleased(key: InputKey) extends Mutation

}
