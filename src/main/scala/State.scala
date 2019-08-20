trait State {

  protected var grid: Grid = new Grid()
  protected var childState: Option[State] = None

  final def simulate(deltaTime: Long): Unit = {

  }

  final def render(): Unit = {
    draw(grid)
    for (child <- childState) child.render()
  }

  protected def idleUpdate(deltaTime: Long): Unit = {

  }

  protected def update(deltaTime: Long): Unit

  protected def draw(grid: Grid): Unit
}