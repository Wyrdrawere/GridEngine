import Grid.{Full, Not, Partial, shouldDraw, positionToPositional}

class Grid
(
  val relativeSize: Vector2 = Vector2(1, 1),
  val relativePosition: Vector2 = Vector2(0, 0),
) {

  private var dimensions: Vector2 = Vector2(0, 0)

  val windowSize: Vector2 = Config.windowSize
  def gridUnit: Vector2 = relativeSize /: dimensions
  def gridTranslation: Vector2 = relativePosition /: gridUnit

  def drawGrid(content: Array[Array[Int]], tileSet: Map[Int, Drawable], offset: Vector2 = Vector2(0, 0)): Unit = {
    dimensions = Vector2(content.length - 2, content(0).length - 2)
    for (x <- content.indices; y <- content(x).indices) {
      val size = gridUnit
      val position = gridTranslation - offset + Vector2(x - 1, y - 1)
      val positional = positionToPositional(Vector2(x, y), Vector2(content.indices.max, content(x).indices.max))
      shouldDraw(positional, offset) match {
        case Full => tileSet(content(x)(y)).drawRectangle(size, gridUnit *: position)
        case Partial(rel, shift, edge) => tileSet(content(x)(y)).drawRectanglePartial(size *: (Vector2(1) - rel), gridUnit *: (position + shift), offset, edge)
        case Not =>
      }
    }
  }

  def drawOnGrid(drawable: Drawable, gridPosition: Vector2, offset: Vector2 = Vector2(0, 0)): Unit = {
    drawable.drawRectangle(gridUnit, gridUnit *: (gridTranslation + offset + Vector2(gridPosition.xi, gridPosition.yi)))
  }

  def drawOnCenter(drawable: Drawable): Unit = drawOnGrid(drawable, dimensions / 2)

}

object Grid {
  sealed trait Positional //todo: the other cases and their implementations (can wait. FUCK diagonals)
  case class Left(outer: Boolean) extends Positional
  case class Right(outer: Boolean) extends Positional
  case class Top(outer: Boolean) extends Positional
  case class Bot(outer: Boolean) extends Positional
  case class CornerNW(outer: Boolean) extends Positional
  case class CornerSW(outer: Boolean) extends Positional
  case class CornerSE(outer: Boolean) extends Positional
  case class CornerNE(outer: Boolean) extends Positional
  case object Center extends Positional
  case object Else extends Positional

  sealed trait DrawMode
  case object Full extends DrawMode
  case object Not extends DrawMode
  case class Partial(relSize: Vector2, positionShift: Vector2, edge: (Boolean, Boolean, Boolean)) extends DrawMode

  def positionToPositional(position: Vector2, maxIndices: Vector2): Positional = {
    (position.x, position.y) match {
      case (1, 1) => CornerSW(false)
      case (1, v) if v == maxIndices.y - 1 => CornerNW(false)
      case (u, 1) if u == maxIndices.x - 1 => CornerSE(false)
      case (u, v) if u == maxIndices.x - 1 && v == maxIndices.y - 1 => CornerNE(false)
      case (0, v) if v > 0 && v < maxIndices.y => Left(true)
      case (maxIndices.x, v) if v > 0 && v < maxIndices.y => Right(true)
      case (u, maxIndices.y) if u > 0 && u < maxIndices.x => Top(true)
      case (u, 0) if u > 0 && u < maxIndices.x => Bot(true)
      case (1, _) => Left(false)
      case (u, _) if u == maxIndices.x - 1 => Right(false)
      case (_, v) if v == maxIndices.y - 1 => Top(false)
      case (_, 1) => Bot(false)
      case (u, v) if u > 0 && u < maxIndices.x - 1 && v > 0 && v < maxIndices.y - 1 => Center
      case _ => Else
    }
  }

  def shouldDraw(positional: Positional, offset: Vector2): DrawMode = {
    positional match {
      case CornerNW(outer) =>
        if (!outer)
          if (offset.x > 0 || offset.y < 0) Partial(offset.abs, Vector2(offset.x.abs, 0), (true, true, false))
          else Full
        else Not
      case CornerSW(outer) =>
        if (!outer)
          if (offset.x > 0 || offset.y > 0) Partial(offset.abs, offset.abs, (true, true, false))
          else Full
        else Not
      case CornerSE(outer) =>
        if (!outer)
          if (offset.x < 0 || offset.y > 0) Partial(offset.abs, Vector2(0, offset.y.abs), (true, true, false))
          else Full
        else Not
      case CornerNE(outer) =>
        if (!outer)
          if (offset.x < 0 || offset.y < 0) Partial(offset.abs, Vector2(0), (true, true, false))
          else Full
        else Not
      case Left(outer) =>
        if (!outer)
          if (offset.x < 0) Full
          else Partial(Vector2(offset.x.abs, 0), Vector2(offset.x.abs, 0), (true, false, false))
        else if (offset.x < 0) Partial(Vector2(1-offset.x.abs, 0), Vector2(1-offset.x.abs, 0), (true, false, true))
        else Not
      case Right(outer) =>
        if (!outer)
          if (offset.x > 0) Full
          else Partial(Vector2(offset.x.abs, 0), Vector2(0), (true, false, false))
        else if (offset.x > 0) Partial(Vector2(1-offset.x.abs, 0), Vector2(0), (true, false, true))
        else Not
      case Top(outer) =>
        if (!outer)
          if (offset.y > 0) Full
          else Partial(Vector2(0, offset.y.abs), Vector2(0), (false, true, false))
        else if (offset.y > 0) Partial(Vector2(0, 1-offset.y.abs), Vector2(0), (false, true, true))
        else Not
      case Bot(outer) =>
        if (!outer)
          if (offset.y < 0) Full
          else Partial(Vector2(0, offset.y.abs), Vector2(0, offset.y.abs), (false, true, false))
        else if (offset.y < 0) Partial(Vector2(0, 1-offset.y.abs), Vector2(0, 1-offset.y.abs), (false, true, true))
        else Not
      case Center => Full
      case Else => Not
    }
  }
}
