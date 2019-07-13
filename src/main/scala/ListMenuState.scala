import Input.{Back, DownArrow, Enter, UpArrow}
import Mutation.{ConfirmMut, DownMut, IdentMut, RemoveChildMut, RemoveReturnMut, UpMut}

case class ListMenuState
(
  cursor: Int,
  items: Map[Int, (Drawable, Mutation)],

  grid: Grid,
  parentState: State,
  childState: Option[State] = None,
  returnMutation: Option[Mutation] = None,
) extends State with Navigatable[Int] {

  override def mutate(mutation: Mutation): State = mutation match {
    case UpMut => this.copy(cursor = if (cursor > 0) cursor - 1 else cursor)
    case DownMut => this.copy(cursor = if (cursor < items.size - 1) cursor + 1 else cursor)
    case ConfirmMut => this.copy(returnMutation = Some(items(cursor)._2))
    case RemoveReturnMut => this.copy(returnMutation = None)
    case RemoveChildMut => this.copy(returnMutation = Some(RemoveChildMut))
    case IdentMut => this
  }

  override def inputToMutation(input: Input): Mutation = input match {
    case UpArrow => UpMut
    case DownArrow => DownMut
    case Enter => ConfirmMut
    case Back => RemoveChildMut
    case _ => IdentMut
  }

  override def draw(): Unit = {
    grid.setDimensions(Vector2(3))
    for(x <- 0 to 3; y <- 0 to 3) {
      grid.drawOnGrid(Color.Blue.translucent(0.5f), Vector2(x,y), Vector2(0))
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

  override def updateChild(child: State): State = this.copy(childState = Some(child))

}
