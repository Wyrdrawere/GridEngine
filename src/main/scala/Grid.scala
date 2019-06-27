class Grid
(
  val size: Vector2 = Config.windowSize,
  val position: Vector2 = Vector2(0,0),

) {
  private var dimensions: Vector2 = Vector2(0,0)
  def indicesX: Range = 1 to dimensions.xi
  def indicesY: Range = 1 to dimensions.yi

  val windowSize: Vector2 = Config.windowSize

  def gridUnit: Vector2 = size/:(dimensions*:windowSize)
  def gridTranslation: Vector2 = position/:size

  def drawGrid(content: Array[Array[Int]], tileSet: Map[Int,Drawable], offset: Vector2 = Vector2(0,0)): Unit = {
    dimensions = Vector2(content.length-2, content(0).length-2)
    for (x <- content.indices; y <- content(x).indices) {
      tileSet(content(x)(y)).drawOnGrid(gridUnit, gridTranslation + offset + Vector2(x-1,y-1))
    }
  }

  def drawOnGrid(drawable: Drawable, gridPosition: Vector2, offset: Vector2 = Vector2(0,0)): Unit = {
    drawable.drawOnGrid(gridUnit, gridTranslation + offset + Vector2(gridPosition.xi-1, gridPosition.yi-1))
  }

  def drawOnCenter(drawable: Drawable): Unit = drawOnGrid(drawable, (dimensions/2).map(v => v+1))

}
