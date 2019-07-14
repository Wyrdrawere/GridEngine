import Mutation.{CancelMut, ChangeJob, ConfirmMut, Direction, Identity, SetChild, SetReturnMutation}

case class JobMenu
(
  cursor: Vector2,
  items: Map[Vector2, (Drawable, Mutation)],

  grid: Grid,
  childState: Option[Stateful] = None,
  returnMutation: Mutation = Identity
) extends Stateful {




  override def mutate(mutation: Mutation): Stateful = mutation match {
    case Direction(Vector2.Up) => this.copy(cursor = if (cursor.y < 1) cursor+Vector2.Up else cursor)
    case Direction(Vector2.Down) => this.copy(cursor = if (cursor.y > 0) cursor+Vector2.Down else cursor)
    case Direction(Vector2.Left) => this.copy(cursor = if (cursor.x > 0) cursor+Vector2.Left else cursor)
    case Direction(Vector2.Right) => this.copy(cursor = if (cursor.x < items.size/2-1) cursor+Vector2.Right else cursor)
    case ConfirmMut => this.copy(returnMutation = items(cursor)._2)
    case CancelMut => this.copy(returnMutation = SetChild(None))
    case SetReturnMutation(m) => this.copy(returnMutation = m)
    case _ => this
  }

  override def draw(grid: Grid): Unit = {

    println(returnMutation)

    grid.setDimensions(Vector2(3))
    for(x <- 0 to 3; y <- 0 to 3) {
      grid.drawOnGrid(Color.Blue.translucent(0.75f), Vector2(x,y), Vector2(0))
    }
    grid.setDimensionsSquare(7)
    val cursorpos = Vector2(cursor.xi % 2, cursor.yi % 2)
    grid.drawOnGrid(Color.Pink.translucent(0.5f), itemsToCoordinate(cursorpos), Vector2(0))
    for(x <- cursor.xi to cursor.xi+1; y <- 0 to 1) {
      items.get(Vector2(x,y)) match {
        case Some(d) => grid.drawOnGrid(d._1, itemsToCoordinate(Vector2(x,y)), Vector2(0))
        case None =>
      }
    }
  }

  def itemsToCoordinate(coord: Vector2): Vector2 = {
    val posX = if (coord.xi % 2 == 0) 3 else 5
    val posY = if (coord.yi % 2 == 0) 5 else 3
    Vector2(posX, posY)
  }
}

object JobMenu {

  def SpriteSheetConvert(sprites: Map[Int, Sprite], cols: Int): Map[Vector2, (Sprite, Mutation)] = {
    var s = sprites.toList.filter(x => x._1 % cols == 0).sortBy(x => x._1)

    var xc = 0
    var yc = 1

    var ret: Map[Vector2, (Sprite, Mutation)] = Map.empty

    for (x <- s.indices) {
      ret = ret.updated(Vector2(xc, yc), (s(x)._2, ChangeJob(x)))
      if (x % 2 == 1) xc = xc+1
      yc = (yc-1).abs % 2
    }
    ret
  }

}