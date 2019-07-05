case class Menu
(
  items: List[Drawable],
  itemsOnScreen: Int
) {

  val cursor: Int = 0
  val firstOnScreen: Int = 0


  def render(grid: Grid): Unit = {
    grid.setDimensions(Vector2(itemsOnScreen))
    for (x <- items.indices) {
      grid.drawOnGrid(items(x), Vector2(itemsOnScreen/2, itemsOnScreen-x), Vector2(0))
    }
  }

}

