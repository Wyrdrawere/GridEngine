class NewGrid
(relativeSize: Vector2,
 relativePosition: Vector2
) {

  var cells: Vector2 = Vector2(1)
  def gridUnit: Vector2 = relativeSize/:cells

  def squareCells(xAmount: Int): Unit = {
    cells = Vector2(xAmount, xAmount * relativeSize.y / relativeSize.x)
  }

  def drawOnGrid(drawable: Drawable, size: Vector2, position: Vector2, layer: Layer = Layer.CenterPlane): Unit = {
    val lowerX = if(position.x < 0) position.x else 0
    val lowerY = if(position.y < 0) position.y else 0
    val upperX = if((position.x+size.x)*gridUnit.x > relativeSize.x) ((position.x+size.x)*gridUnit.x-relativeSize.x)/gridUnit.x else 0
    val upperY = if((position.y+size.y)*gridUnit.y > relativeSize.y) ((position.y+size.y)*gridUnit.y-relativeSize.y)/gridUnit.y else 0
    val lower = Vector2(lowerX, lowerY)
    val upper = Vector2(upperX, upperY)
    if((lower.abs+upper.abs).x < 1 && (lower.abs+upper.abs).y < 1) {
      drawable.drawRectanglePartialProto((size-lower.abs-upper.abs)*:gridUnit, relativePosition+(position-lower)*:gridUnit, layer, lower.abs, upper.abs)
    }
  }

  def drawGrid(drawables: Array[Array[Drawable]], layer: Layer = Layer.Background, offset: Vector2 = Vector2(0)): Unit = {
    cells = Vector2(drawables.length-2, drawables(0).length-2)
    for(x <- drawables.indices; y <- drawables(x).indices) {
      drawOnGrid(drawables(x)(y), Vector2(1), Vector2(x-1,y-1)+offset, layer)
    }
  }
}
