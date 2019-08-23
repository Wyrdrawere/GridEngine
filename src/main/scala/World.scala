

trait World {

  type Listener = PartialFunction[Event, (Long, World) => Unit]

  private var entities: List[Entity] = List.empty

  def emit(event: Event): Unit

}

trait Event

trait Entity {

  def update(deltaTime: Long, world: World): Unit
  def render(grid: Grid): Unit

}