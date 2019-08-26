package engine

import util.Config

trait World {

  private var worldState: Map[State, List[Event]] = Map.empty

  def emit(event: Event, state: State = worldState.keys.head): Unit = {
    worldState = worldState.updated(state, event +: worldState(state))
  }

  def push(state: State): Unit = {
    worldState = ((state, List.empty[Event]) +: worldState.toList).toMap
    state.init()
  }

  def pop(): Unit = {
    worldState = worldState.tail
  }

  def update(deltaTime: Long): Unit = {

    println(worldState)

    worldState.keys.foreach(_.idleUpdate(this, deltaTime))
    worldState.keys.head.update(this, deltaTime)
    worldState.foreach { case (state, events) =>
      events.foreach(e => {
        println(e)
        val pf = state.default orElse state.mutate
        if (pf.isDefinedAt(e)) {
          pf(e)(this, state)
        }
        worldState = worldState.updated(state, List.empty)
      })
    }
  }

  def render(): Unit = {
    worldState.keys.toList.reverse.foreach(_.render())
  }


}
