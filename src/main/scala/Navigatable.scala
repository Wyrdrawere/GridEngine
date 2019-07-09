trait Navigatable[T] {

  val cursor: T
  val items: Map[T, Drawable]

  def navigate(input: Input): T
  def show(coordinate: T): Vector2

}
