trait Navigatable[T] {

  protected var cursor: T
  val items: Map[T, Drawable]

  def navigate(input: Input): T

}
