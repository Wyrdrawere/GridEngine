import Input.{DownArrow, UpArrow}

case class ListMenu
(
  _cursor: Int,
  _items: List[Text]
) extends Navigatable[Int]
  with Stateful {
  override val cursor: Int = _cursor
  override val items: Map[Int, Text] = _items.map(x => (_items.indexOf(x), x)).toMap
  val gap = 0.25f

  override def navigate(input: Input): Int = input match {
    case UpArrow => if(cursor > 0) cursor-1 else 0
    case DownArrow => if (cursor < items.size-1) cursor+1 else items.size-1
    case _ => cursor
  }

  override def show(coordinate: Int): Vector2 = ???

  override def simulate(deltaTime: Long, input: Input): ListMenu = this.copy(_cursor = navigate(input))

  override def render(grid: Grid): Unit = {
    val dim = grid.getDimensions
    for (x <- items.toList.indices) {
      grid.drawOnGrid(items(x), Vector2(dim.x/4, dim.y-(x+1)-(x*gap)), Vector2(0), Vector2(items(x).string.length))
    }
    grid.drawOnGrid(Color.Green, Vector2((dim.x/4)-1, dim.y-(cursor+1)-(cursor*gap)), Vector2(0))
  }
}
