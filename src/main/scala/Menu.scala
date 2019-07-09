case class Menu
(
  items: List[Text],
  itemSlots: Int
) {

  val cursor: Int = 0
  val firstOnScreen: Int = 0
  val gap = 0.5f


  def render(grid: Grid): Unit = {
    grid.setDimensions(Vector2(10, itemSlots))
    for (x <- items.indices) {
      grid.drawOnGrid(items(x), Vector2(1, itemSlots-(2*(x+1))), Vector2(0), Vector2(items(x).string.length))
    }
  }

}

