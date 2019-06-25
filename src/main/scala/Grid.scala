case class Grid
(
  content: Array[Array[Int]],
  tileSet: Map[Int, Drawable]
)
  extends Drawable
{
  override def drawRectangle(position: Vector2, size: Vector2, parentSize: Vector2): Unit = {
    val xUnit = size.x/(content.length-2)
    val yUnit = size.y/(content(0).length-2)
    for (x <- 0 until content.length-1; y <- 1 until content(x).length-1) {
      tileSet(content(x)(y)).drawRectangle(Vector2((x-1)*xUnit,(y-1)*yUnit),Vector2(xUnit, yUnit))
    }
  }
}
