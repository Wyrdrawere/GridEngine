trait Drawable {
  def drawRectangle(size: Vector2, position: Vector2): Unit
  def drawRectanglePartial(size: Vector2, position: Vector2, offset: Vector2, edge: (Boolean, Boolean, Boolean)): Unit = drawRectangle(size, position)
  //todo: edge is super ugly, need better way. Needs to happen alongside gridrefactoring2 (far, FAR future)

  def drawRectanglePartialProto(size: Vector2, position: Vector2, lowerOffset: Vector2, upperOffset: Vector2): Unit = drawRectangle(size, position)
}
