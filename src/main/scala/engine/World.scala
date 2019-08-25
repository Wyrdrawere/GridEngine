package engine

trait World {

  private var events: List[Event] = List.empty
  private var states: List[State] = List.empty

  def emit(event: Event): Unit = {events = event +: events}

  def push(state: State): Unit = {
    states = state +: states
    state.init()
  }

  def pop(): Unit = {
    states = states.tail
  }

  def update(deltaTime: Long): Unit = {
    states.head.update(this, deltaTime)
    val currentEvents = events.reverse
    events = List.empty
    for (event <- currentEvents) {
      states.head.mutate(event)(this)
    }
  }

  def render(deltaTime: Long): Unit = {
    states.foreach(_.render(this, deltaTime))
  }


}
