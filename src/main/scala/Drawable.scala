trait Drawable {
  def drawRectangle(size: Vector2, position: Vector2): Unit
  def drawOnGrid(gridUnit: Vector2, gridPosition: Vector2): Unit = {
    this.drawRectangle(gridUnit, gridUnit*:gridPosition)
  }
}
