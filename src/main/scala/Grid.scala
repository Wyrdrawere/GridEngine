class Grid
(
  val relativeSize: Vector2 = Vector2(1,1),
  val relativePosition: Vector2 = Vector2(0,0),

) {

  //todo: make offset work without breaking bounds
  private var dimensions: Vector2 = Vector2(0,0)
  def indicesX: Range = 1 until dimensions.xi
  def indicesY: Range = 1 until dimensions.yi

  val windowSize: Vector2 = Config.windowSize

  def gridUnit: Vector2 = relativeSize/:dimensions
  def gridTranslation: Vector2 = relativePosition/:gridUnit

  def drawGrid(content: Array[Array[Int]], tileSet: Map[Int, Drawable], offset: Vector2 = Vector2(0,0)): Unit = {
    dimensions = Vector2(content.length-2, content(0).length-2)
    for (x <- content.indices; y <- content(x).indices) {
      var size = gridUnit
      var position = gridTranslation + offset + Vector2(x-1,y-1)
      var positional = coordsToPositional(Vector2(x,y), Vector2(content.indices.max, content(x).indices.max))
      shouldDraw(positional, offset) match {
        case Full => tileSet(content(x)(y)).drawRectangle(size, gridUnit*:position)
        case Partial(rel, shift, edge) => tileSet(content(x)(y)).drawRectanglePartial(size*:(Vector2(1)-rel), gridUnit*:(position+shift), offset, edge)
        case Not =>
      }

    }
  }

  def drawOnGrid(drawable: Drawable, gridPosition: Vector2, offset: Vector2 = Vector2(0,0)): Unit = {
    drawable.drawRectangle(gridUnit, gridUnit*:(gridTranslation + offset + Vector2(gridPosition.xi, gridPosition.yi)))
  }

  def drawOnCenter(drawable: Drawable): Unit = drawOnGrid(drawable, dimensions/2)


  sealed trait Positional
  case object OuterLeft extends Positional
  case object OuterRight extends Positional
  case object OuterTop extends Positional
  case object OuterBot extends Positional
  case object OuterNW extends Positional
  case object OuterNE extends Positional
  case object OuterSW extends Positional
  case object OuterSE extends Positional
  case object InnerLeft extends Positional
  case object InnerRight extends Positional
  case object InnerTop extends Positional
  case object InnerBot extends Positional
  case object InnerNW extends Positional
  case object InnerNE extends Positional
  case object InnerSW extends Positional
  case object InnerSE extends Positional
  case object Center extends Positional

  def coordsToPositional(coords: Vector2, maxIndices: Vector2): Positional = {
    (coords.x, coords.y) match {
      case (0,0) => OuterSW
      case (0, maxIndices.y) => OuterNW
      case (maxIndices.x, maxIndices.y) => OuterNE
      case (maxIndices.x, 0) => OuterSE

      case (1,1) => InnerSW
      case (1, v) if v == maxIndices.y-1 => InnerNW
      case (u, v) if u == maxIndices.x-1 && v == maxIndices.y-1=> InnerNE
      case (u, 1) if u == maxIndices.x-1 => InnerSE

      case (0,_) => OuterLeft
      case (maxIndices.x,_) => OuterRight
      case (_,maxIndices.y) => OuterTop
      case (_,0) => OuterBot

      case (1,_) => InnerLeft
      case (u,_) if u == maxIndices.x-1 => InnerRight
      case (_,v) if v == maxIndices.y-1 => InnerTop
      case (_,1) => InnerBot

      case _ => Center
    }
  }

  def shouldDrawPositional(positional: Positional, offset: Vector2): Boolean = {
    positional match {
      case OuterLeft => offset.x < 0
      case OuterRight => offset.x > 0
      case OuterTop => offset.y > 0
      case OuterBot => offset.y < 0
      case OuterNW => offset.x < 0 && offset.y > 0
      case OuterNE => offset.x > 0 && offset.y > 0
      case OuterSW => offset.x < 0 && offset.y < 0
      case OuterSE => offset.x > 0 && offset.y < 0
      case _ => true
    }
  }

  sealed trait DrawMode
  case object Full extends DrawMode
  case object Not extends DrawMode
  case class Partial(relSize: Vector2, positionShift: Vector2, edge: (Boolean, Boolean)) extends DrawMode

  def shouldDraw(positional: Positional, offset: Vector2): DrawMode = {
    positional match {
      case OuterLeft => if (offset.x < 0)
        Partial(
          Vector2(offset.x.abs,0),
          Vector2(offset.x.abs,0),
          (true, false))
        else Not
      case OuterRight => if (offset.x > 0)
        Partial(
          Vector2(offset.x.abs, 0),
          Vector2(0),
          (true, false))
        else Not
      case OuterTop => if (offset.y > 0)
        Partial(
          Vector2(0, offset.y.abs),
          Vector2(0),
          (false, true))
        else Not
      case OuterBot => if (offset.y < 0)
        Partial(
          Vector2(0, offset.y.abs),
          Vector2(0, offset.y.abs),
          (false, true))
        else Not
      case OuterNW => if (offset.x < 0 && offset.y > 0)
        Partial(
          offset.abs,
          offset.abs,
          (true, true))
        else Not
      case OuterNE => if (offset.x > 0 && offset.y > 0)
        Partial(
          offset.abs,
          Vector2(0),
          (true, true))
        else Not
      case OuterSW => if (offset.x < 0 && offset.y < 0)
        Partial(
          offset.abs,
          Vector2(offset.x.abs, 0),
          (true, true))
        else Not
      case OuterSE => if (offset.x > 0 && offset.y < 0)
        Partial(
          offset.abs,
          Vector2(0, offset.y.abs),
          (true, true))
        else Not
      case InnerLeft => if (offset.x < 0) Full else
        Partial(
          Vector2(offset.x.abs,0),
          Vector2(offset.x.abs,0),
          (true, false))
      case InnerRight => if (offset.x > 0) Full else
        Partial(
          Vector2(offset.x.abs, 0),
          Vector2(0),
          (true, false))
      case InnerTop => if (offset.y > 0) Full else
        Partial(
          Vector2(0, offset.y.abs),
          Vector2(0),
          (false, true))
      case InnerBot => if (offset.y < 0) Full else
        Partial(
          Vector2(0, offset.y.abs),
          Vector2(0, offset.y.abs),
          (false, true))
      case InnerNW => if (offset.x <= 0 && offset.y >= 0) Full else
        Partial(
          offset.abs,
          Vector2(offset.abs.x,0),
          (true, true))
      case InnerNE => if (offset.x >= 0 && offset.y >= 0) Full else
        Partial(
          offset.abs,
          Vector2(0),
          (true, true))
      case InnerSW => if (offset.x <= 0 && offset.y <= 0) Full else
        Partial(
          offset.abs,
          offset.abs,
          (true, true))
      case InnerSE =>if (offset.x >= 0 && offset.y <= 0) Full else
        Partial(
          offset.abs,
          Vector2(0, offset.y.abs),
          (true, true))
      case Center => Full
    }
  }

}
