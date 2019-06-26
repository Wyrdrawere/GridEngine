case class Tile
(
  content: List[Int],
  tileSet: Map[Int, Drawable]
) extends Drawable {
  override def drawRectangle(size: Vector2, position: Vector2): Unit = {
    for (d <- content) {
      tileSet(d).drawRectangle(size, position)
    }
  }
}
