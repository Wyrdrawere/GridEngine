sealed trait Mutation

object Mutation {

  final case object IdentMut extends Mutation
  final case object UpMut extends Mutation
  final case object DownMut extends Mutation
  final case object LeftMut extends Mutation
  final case object RightMut extends Mutation

}
