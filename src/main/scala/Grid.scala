class Grid
(
  val relativeSize: Vector2 = Vector2(1,1),
  val relativePosition: Vector2 = Vector2(0,0),

) {

  //todo: make offset work without breaking bounds
  private var dimensions: Vector2 = Vector2(0,0)
  def indicesX: Range = 0 until dimensions.xi
  def indicesY: Range = 0 until dimensions.yi

  val windowSize: Vector2 = Config.windowSize

  def gridUnit: Vector2 = relativeSize/:dimensions
  def gridTranslation: Vector2 = relativePosition/:gridUnit

  def drawGrid(content: Array[Array[Int]], tileSet: Map[Int, Drawable], offset: Vector2 = Vector2(0,0)): Unit = {
    dimensions = Vector2(content.length, content(0).length)
    for (x <- indicesX; y <- indicesY) {
      var shouldDraw = true
      var size = gridUnit
      var position = gridTranslation + offset + Vector2(x,y)
      if(isAlwaysViewable(x,y)) {

      }
      else {
        size = size *: (Vector2(1)-(Vector2(if (x > 0 && x < indicesX.max) 0 else 1, if (y > 0 && y < indicesY.max) 0 else 1)*:offset.abs))
        (x, y) match {
          case (0, 0) => {
            shouldDraw = offset.x > 0 && offset.y > 0
            position = position + offset.abs
          }
          case (u, 0) if u == indicesX.max => {
            shouldDraw = offset.x < 0 && offset.y > 0
            position = position + (offset.abs*:Vector2.Up)
          }
          case (0, v) if v == indicesY.max => {
            shouldDraw = offset.x > 0 && offset.y < 0
            position = position + (offset.abs*:Vector2.Right)
          }
          case (u, v) if u == indicesX.max && v == indicesY.max => {
            shouldDraw = offset.x < 0 && offset.y < 0
          }
          case (0, _) => {
            shouldDraw = offset.x > 0
            position = position + (offset.abs*:Vector2.Right)
          }
          case (_, 0) => {
            shouldDraw = offset.y > 0
            position = position + (offset.abs*:Vector2.Up)
          }
          case (u, _) if u == indicesX.max => {
            shouldDraw = offset.x < 0
          }
          case (_, v) if v == indicesY.max => {
            shouldDraw = offset.y < 0
          }
        }
      }
      if (shouldDraw) {
        tileSet(content(x)(y)).drawRectangle(size, gridUnit*:position)
      }
    }
  }

  def drawOnGrid(drawable: Drawable, gridPosition: Vector2, offset: Vector2 = Vector2(0,0)): Unit = {
    drawable.drawRectangle(gridUnit, gridUnit*:(gridTranslation + offset + Vector2(gridPosition.xi, gridPosition.yi)))
  }

  def drawOnCenter(drawable: Drawable): Unit = drawOnGrid(drawable, (dimensions/2).map(v => v+1))

  def isAlwaysViewable(x: Int,y: Int): Boolean = x > 0 && x < indicesX.max && y > 0 && y < indicesY.max

}
