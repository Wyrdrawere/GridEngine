import Mutation.{CancelMut, ConfirmMut, Direction, DownMut, Identity, MakeSubMenu, SetChild, SetReturnMutation, UpMut}

case class ListMenu
(
  cursor: Int,
  items: Map[Int, (Drawable, Mutation)],

  grid: Grid,
  childState: Option[Stateful] = None,
  returnMutation: Mutation = Identity

) extends Stateful {
  override def mutate(mutation: Mutation): Stateful = mutation match {
    case Direction(Vector2.Up) => this.copy(cursor = if (cursor > 0) cursor - 1 else cursor)
    case Direction(Vector2.Down) => this.copy(cursor = if (cursor < items.size - 1) cursor + 1 else cursor)
    case ConfirmMut => items(cursor)._2 match {
      case r@SetReturnMutation(m) => mutate(r)
      case m@_ => mutate(m)
    }
    case CancelMut => this.copy(returnMutation = SetChild(None))
    case MakeSubMenu => this.copy(childState = Some(JobMenu(Vector2(0), new Grid(Vector2(0.75f,0.75f), Vector2(0, 0.25f)))))
    case SetReturnMutation(m) => this.copy(returnMutation = m)
    case _ => this
  }

  override def draw(grid: Grid): Unit = {
    grid.setDimensions(Vector2(3))
    for(x <- 0 to 3; y <- 0 to 3) {
      grid.drawOnGrid(Color.Blue.translucent(0.75f), Vector2(x,y), Vector2(0))
    }
    grid.setDimensionsSquare(items.toList.map(x => x._2._1).collect{case t: Text => t}.map(t => t.string.length).max+4)
    val dim = grid.getDimensions
    for(x <- items.toList.indices) {
      val size = items(x)._1 match {
        case Text(s, f) => Vector2(s.length)
        case _ => Vector2(1)
      }
      grid.drawOnGrid(items(x)._1, Vector2(3, dim.y-2-x), Vector2(0), size)
    }
    grid.drawOnGrid(Color.Pink, Vector2(2, dim.y-2-cursor), Vector2(0))
  }
}
