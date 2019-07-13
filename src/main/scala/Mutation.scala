sealed trait Mutation

object Mutation {

  final case object IdentMut extends Mutation
  final case object UpMut extends Mutation
  final case object DownMut extends Mutation
  final case object LeftMut extends Mutation
  final case object RightMut extends Mutation
  final case object OpenMenuMut extends Mutation
  final case object ConfirmMut extends Mutation
  final case object RemoveReturnMut extends Mutation
  final case object RemoveChildMut extends Mutation
  final case class ChangeColorMut(color: Color) extends Mutation

}
