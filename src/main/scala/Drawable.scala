trait Drawable {
  def drawRectangle(size: Vector2, position: Vector2): Unit
  def drawOnGrid(gridUnit: Vector2, gridPosition: Vector2): Unit = {
    this.drawRectangle(gridUnit, gridUnit*:gridPosition)
  }
}

object Drawable {

  //todo: grid needs its own class
  def WindowToGridDimensions
  (
    gridSize: Vector2,
    position: Vector2,
    cellAmount: Vector2,
    windowSize: Vector2 = Config.windowSize
  ): (Vector2, Vector2) = {
    val gridUnit = gridSize/:(cellAmount*:windowSize)
    val gridPosition = (position/:gridUnit)/:windowSize
    (gridUnit, gridPosition)
  }

  def drawGrid
  (
    content: Array[Array[Int]],
    tileSet: Map[Int, Drawable],
    size: Vector2 = Config.windowSize,
    position: Vector2 = Vector2(0, 0),
    offset: Vector2 = Vector2(0, 0)
  ): Unit = {
    val dim = WindowToGridDimensions(size, position, Vector2(content.length-2, content(0).length-2))
    for (x <- content.indices; y <- content(x).indices) {
      tileSet(content(x)(y)).drawOnGrid(
        dim._1,
        dim._2 + Vector2(x-1,y-1) + offset
      )
    }
  }

  def drawOnGrid
  (
    drawable: Drawable,
    gridPosition: Vector2,
    gridDimensions: Vector2,
    size: Vector2 = Config.windowSize,
    position: Vector2 = Vector2(0, 0),
    offset: Vector2 = Vector2(0, 0)
  ): Unit = {
    val dim = WindowToGridDimensions(size, position, gridDimensions)
    drawable.drawOnGrid(
      dim._1,
      dim._2 + Vector2(gridPosition.x - 1, gridPosition.y - 1) + offset,
    )
  }
}