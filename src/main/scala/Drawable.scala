trait Drawable {
  def drawRectangle(size: Vector2, position: Vector2): Unit
  def drawOnGridPartial(gridUnit: Vector2, gridPosition: Vector2, offset: Vector2, cutoff: Vector2): Unit = drawRectangle(gridUnit, gridPosition)
}
