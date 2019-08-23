trait World {

  type Listener = PartialFunction[Event, (Long, World) => Unit]

  protected def entities: List[Entity] = List.empty
  private var events: List[Event] = List.empty
  private var listeners: List[Listener] = List.empty

  def emit(event: Event): Unit = {events = event +: events}

  def register(listener: Listener): Unit = {listeners = listener +: listeners}

  def update(deltaTime: Long): Unit = {
    val currentEvents = events.reverse
    events = List.empty
    for (event <- currentEvents; listener <- listeners) {
      if(listener.isDefinedAt(event)) {
        listener(event)(deltaTime, this)
      }
    }
    for(entity <- entities) {
      entity.update(deltaTime, this)
    }
  }

}

trait Event

trait Entity {

  def update(deltaTime: Long, world: World): Unit
  def render(grid: Grid): Unit

}
