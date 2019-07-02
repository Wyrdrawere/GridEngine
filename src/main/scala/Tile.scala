case class Tile
(
  content: List[Sprite],
) extends Drawable {
  override def drawRectangle(size: Vector2, position: Vector2): Unit = {
    for (s <- content) {
      s.drawRectangle(size, position)
    }
  }

  override def drawRectanglePartial(size: Vector2, position: Vector2, offset: Vector2, edge: (Boolean, Boolean, Boolean)): Unit = {
    for (s <- content) {
      s.drawRectanglePartial(size, position, offset, edge)
    }
  }
}
