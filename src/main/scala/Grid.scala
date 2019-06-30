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

  def drawGrid2(content: Array[Array[Int]], tileSet: Map[Int, Drawable], offset: Vector2 = Vector2(0,0)): Unit = {
    dimensions = Vector2(content.length-2, content(0).length-2)
    for (x <- content.indices; y <- content(x).indices) {
      var size = gridUnit
      var position = gridTranslation + offset + Vector2(x-1,y-1)
      var positional = coordsToPositional(Vector2(x,y), Vector2(content.indices.max, content(x).indices.max))

      if (positional != Center) {

        var edge = (false, false)
        positional match {
          case OuterLeft => if (offset.x > 0) {
            size = size*:(Vector2(1)-offset.abs)
            position = position + offset.abs
            tileSet(content(x)(y)).drawRectanglePartial(size, gridUnit*:position, offset, positionalToEdge(positional))
          } else {
            tileSet(content(x)(y)).drawRectangle(size, gridUnit*:position)
          }
          case OuterRight => if (offset.x > 0) {
            tileSet(content(x)(y)).drawRectangle(size, gridUnit*:position)
          } else {
            size = size*:(Vector2(1)-offset.abs)
            position = position + offset.abs
            tileSet(content(x)(y)).drawRectanglePartial(size, gridUnit*:position, offset, positionalToEdge(positional))
          }
          case OuterTop => if (offset.y > 0) {
            tileSet(content(x)(y)).drawRectangle(size, gridUnit*:position)
          } else {
            size = size*:(Vector2(1)-offset.abs)
            position = position + offset.abs
            tileSet(content(x)(y)).drawRectanglePartial(size, gridUnit*:position, offset, positionalToEdge(positional))
          }
          case OuterBot =>
          case OuterNW =>
          case OuterNE =>
          case OuterSW =>
          case OuterSE =>
          case InnerLeft => if (offset.x > 0) {
            tileSet(content(x)(y)).drawRectangle(size, gridUnit*:position)
          } else {
            size = size*:(Vector2(1)-offset.abs)
            position = position
            tileSet(content(x)(y)).drawRectanglePartial(size, gridUnit*:position, offset, positionalToEdge(positional))
          }
          case InnerRight => if (offset.x > 0) {
            size = size*:(Vector2(1)-offset.abs)
            position = position
            tileSet(content(x)(y)).drawRectanglePartial(size, gridUnit*:position, offset, positionalToEdge(positional))
          } else {
            tileSet(content(x)(y)).drawRectangle(size, gridUnit*:position)
          }
          case InnerTop =>
          case InnerBot => if (offset.y > 0) {
            tileSet(content(x)(y)).drawRectangle(size, gridUnit*:position)
          } else {
            size = size*:(Vector2(1)-offset.abs)
            position = position + offset.abs
            tileSet(content(x)(y)).drawRectanglePartial(size, gridUnit*:position, offset, positionalToEdge(positional))
          }
          case InnerNW =>
          case InnerNE =>
          case InnerSW =>
          case InnerSE =>
          case _ =>
        }


      } else {
        tileSet(content(x)(y)).drawRectangle(size, gridUnit*:position)
      }
    }
  }

  def drawGrid(content: Array[Array[Int]], tileSet: Map[Int, Drawable], offset: Vector2 = Vector2(0,0)): Unit = {
    dimensions = Vector2(content.length-2, content(0).length-2)
    for (x <- content.indices; y <- content(x).indices) {
      var size = gridUnit
      var position = gridTranslation + offset + Vector2(x-1,y-1)
      if(isAlwaysVisible(x,y)) {
        tileSet(content(x)(y)).drawRectangle(size, gridUnit*:position)
      }
      else {
        var edge = (false, false)
        var shouldDraw = false
        size = size *: (Vector2(1)-(Vector2(if (x > 0 && x < indicesX.max) 0 else 1, if (y > 0 && y < indicesY.max) 0 else 1)*:offset.abs))
        (x, y) match {
          case (0, 0) => {
            edge = (true, true)
            position = position + offset.abs
            shouldDraw = offset.x > 0 && offset.y > 0
          }
          case (u, 0) if u == indicesX.max => {
            edge = (true, true)
            position = position + (offset.abs*:Vector2.Up)
            shouldDraw = offset.x < 0 && offset.y > 0
          }
          case (0, v) if v == indicesY.max => {
            edge = (true, true)
            position = position + (offset.abs*:Vector2.Right)
            shouldDraw = offset.x > 0 && offset.y < 0
          }
          case (u, v) if u == indicesX.max && v == indicesY.max => {
            edge = (true, true)
            shouldDraw = offset.x < 0 && offset.y < 0
          }
          case (0, _) => {
            edge = (true, false)
            position = position + (offset.abs*:Vector2.Right)
            shouldDraw = offset.x > 0
          }
          case (_, 0) => {
            edge = (false, true)
            position = position + (offset.abs*:Vector2.Up)
            shouldDraw = offset.y > 0
          }
          case (u, _) if u == indicesX.max => {
            edge = (true, false)
            shouldDraw = offset.x < 0
          }
          case (_, v) if v == indicesY.max => {
            edge = (false, true)
            shouldDraw = offset.y < 0
          }
          case _ =>
        }
        if (true) {
          tileSet(content(x)(y)).drawRectanglePartial(size, gridUnit*:position, offset, edge)
        }
      }
    }
  }

  def drawOnGrid(drawable: Drawable, gridPosition: Vector2, offset: Vector2 = Vector2(0,0)): Unit = {
    drawable.drawRectangle(gridUnit, gridUnit*:(gridTranslation + offset + Vector2(gridPosition.xi, gridPosition.yi)))
  }

  def drawOnCenter(drawable: Drawable): Unit = drawOnGrid(drawable, dimensions/2)

  def isAlwaysVisible(x: Int, y: Int): Boolean = x > 0 && x < indicesX.max && y > 0 && y < indicesY.max

  def isVisible(position: Vector2, maxIndex: Vector2, offset: Vector2): Boolean = {
    false
  }

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

  def positionalToEdge(positional: Positional): (Boolean, Boolean) = {
    positional match {
      case OuterLeft => (true, false)
      case OuterRight => (true, false)
      case OuterTop => (false, true)
      case OuterBot => (false, true)
      case OuterNW => (true,true)
      case OuterNE => (true,true)
      case OuterSW => (true,true)
      case OuterSE => (true,true)
      case InnerLeft => (true, false)
      case InnerRight => (true, false)
      case InnerTop => (false, true)
      case InnerBot => (false, true)
      case InnerNW => (true,true)
      case InnerNE => (true,true)
      case InnerSW => (true,true)
      case InnerSE => (true,true)
      case Center => (false, false)
    }
  }

}
