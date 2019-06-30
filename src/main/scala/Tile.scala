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

  override def drawRectanglePartial(size: Vector2, position: Vector2, offset: Vector2, edge: (Boolean, Boolean, Boolean)): Unit = {
    for (d <- content) {
      tileSet(d).drawRectanglePartial(size, position, offset, edge)
    }
  }

}
