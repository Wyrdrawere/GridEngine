trait Navigatable[T] {

  val cursor: T
  val items: Map[T, (Drawable, Mutation)]
}
