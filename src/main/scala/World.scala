trait World {

  type Listener = PartialFunction[Event, (Long, World) => Unit]

  private def _entities: List[Entity] = List.empty
  private var events: List[Event] = List.empty
  private var listeners: List[Listener] = List.empty

  def entities: List[Entity] = _entities

  def selectEntites(component: Component): List[Entity] = ???

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

    }
  }

}

trait Event

trait Entity {

  private var cs: List[Component] = List.empty

  def components: List[Component] = cs

  def attach(component: Component): Unit = {
    cs = component +: cs
  }

  def detach[C <: Component](): Unit = {

  }

  def has(component: Component): Boolean = ???

}

trait Component

case class HPComponent(var hp: Int) extends Component

