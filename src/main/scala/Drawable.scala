trait Drawable {
  def drawRectangle(size: Vector2, position: Vector2): Unit
  def drawRectanglePartial(size: Vector2, position: Vector2, offset: Vector2, edge: (Boolean, Boolean)): Unit = drawRectangle(size, position)
  //todo: edge is ugly, need better way
}
