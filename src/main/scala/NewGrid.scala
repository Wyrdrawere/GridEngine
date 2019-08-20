class NewGrid
(relativeSize: Vector2,
 relativePosition: Vector2
) {

  var cells: Vector2 = Vector2(16)
  val gridUnit: Vector2 = relativeSize/:cells

  def drawOnGrid(drawable: Drawable, size: Vector2, position: Vector2): Unit = {
    drawable.drawRectangle(size*:gridUnit, relativePosition+(position+Vector2(2))*:gridUnit)

    val lower = Vector2(0)
    val upper = Vector2(0.5f)

    drawable.drawRectanglePartialProto((size-lower-upper)*:gridUnit, relativePosition+position*:gridUnit, lower, upper)
  }

}
