trait Drawable {
  def drawRectRelative(position: Vector2, size: Vector2): Unit //todo: requires windowsize
  def drawRectGrid(position: Vector2, gridSize: Vector2): Unit = this.drawRectRelative(position**gridSize, gridSize)
}
