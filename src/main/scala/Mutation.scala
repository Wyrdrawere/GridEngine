sealed trait Mutation

object Mutation {


  final case object UpMut extends Mutation
  final case object DownMut extends Mutation
  final case object LeftMut extends Mutation
  final case object RightMut extends Mutation
  final case object OpenMenuMut extends Mutation
  final case object ConfirmMut extends Mutation
  final case object CancelMut extends Mutation
  final case object PauseMut extends Mutation
  final case object RemoveReturnMut extends Mutation
  final case object RemoveChildMut extends Mutation
  final case class ChangeColorMut(color: Color) extends Mutation

  //todo: phase out above

  final case object Identity extends Mutation
  final case class SetChild(state: Option[Stateful]) extends Mutation
  final case class SetReturnMutation(mutation: Mutation) extends Mutation
  final case class Direction(dir: Vector2) extends Mutation


}
