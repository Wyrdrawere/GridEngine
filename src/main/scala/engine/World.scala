package engine

import engine.GlobalEvent.{Pop, Push}
import util.Config

trait World {

  private var globalEvents: List[GlobalEvent] = List.empty

  private var worldState: Map[State, List[Event]] = Map.empty

  def emit(event: Event, state: State): Unit = {
    for (se <- worldState.get(state)) {
      worldState = worldState.updated(state, event +: worldState(state))
    }
  }

  def emit(globalEvent: GlobalEvent): Unit = {
    globalEvents = globalEvent +: globalEvents
  }

  private def push(state: State): Unit = {
    worldState = ((state, List.empty[Event]) +: worldState.toList).toMap
    state.init()
  }

  private def pop(): Unit = {
    worldState = worldState.removed(worldState.keys.head)
  }

  def update(deltaTime: Long): Unit = {

    println(worldState)

    for (g <- globalEvents) g match {
      case Push(state) => push(state)
      case Pop => pop()
    }
    globalEvents = List.empty

    worldState.keys.foreach(_.idleUpdate(this, deltaTime))
    worldState.keys.head.update(this, deltaTime)
    worldState.foreach { case (state, events) =>
      events.reverse.foreach(e => {
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
