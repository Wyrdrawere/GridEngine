trait Drawable {
  def drawRectangle(position: Vector2, size: Vector2, parentSize: Vector2 = Config.windowSize): Unit
}
