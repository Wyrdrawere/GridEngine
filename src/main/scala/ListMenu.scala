import Input.{Back, DownArrow, UpArrow}
import Stateful.{NextState, Remove, Same}

case class ListMenu
(
  _cursor: Int,
  _items: List[Text],
  next: NextState = Same
) extends Navigatable[Int]
  with Stateful {

  override val nextState: NextState = next
  private var end: Boolean = false //todo: rework stateful to not need this, ever

  override protected var cursor: Int = _cursor
  override val items: Map[Int, Text] = _items.map(x => (_items.indexOf(x), x)).toMap
  val gap = 0.25f

  override def navigate(input: Input): Int = {
    input match {
      case UpArrow => if(cursor > 0) cursor-1 else 0
      case DownArrow => if (cursor < items.size-1) cursor+1 else items.size-1
      case Back => {
        end = true
        cursor
      }
      case _ => cursor
    }
  }

  override def show(coordinate: Int): Vector2 = ???

  override def simulate(deltaTime: Long, input: Input): ListMenu = {
    this.copy(_cursor = navigate(input), next = if(end) Remove else Same)
  }

  override def render(grid: Grid): Unit = {
    grid.setDimensionsSquare(15)
    val dim = grid.getDimensions
    for (x <- items.toList.indices) {
      grid.drawOnGrid(items(x), Vector2(dim.x/4, dim.y-(x+1)-(x*gap)), Vector2(0), Vector2(items(x).string.length))
    }
    grid.drawOnGrid(Color.Green, Vector2((dim.x/4)-1, dim.y-(cursor+1)-(cursor*gap)), Vector2(0))
  }
}

object ListMenu {

  val testMenu: ListMenu = {
    val text = Text("HELLO WORLD!", Text.DarkGrayFont)
    val t1 = Text("STATUS", Text.DarkGrayFont)
    val t2 = Text("SKILLS", Text.DarkGrayFont)
    val t3 = Text("EQUIPMENT", Text.DarkGrayFont)
    val t4 = Text("JOBS", Text.DarkGrayFont)

    val testList: List[Text] = List(t1,t2,t3,t4)
    ListMenu(0, testList)
  }

}