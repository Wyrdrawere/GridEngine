trait State {

  protected var grid: Grid = new Grid()
  protected var childState: Option[State] = None

  final def simulate(deltaTime: Long): Unit = childState match {
    case Some(child) => child.simulate(deltaTime)
    case None => this.update(deltaTime)
  }

  final def render(): Unit = {
    draw(grid)
    for (child <- childState) child.render()
  }

  protected def update(deltaTime: Long): Unit

  protected def draw(grid: Grid): Unit
}