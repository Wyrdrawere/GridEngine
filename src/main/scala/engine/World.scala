package engine

import engine.GlobalEvent.{Pop, Push}
import util.Config

trait World { //todo: this could potentially be an object, would be an annoying refactoring, think it through first

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

    //todo: rewrite to not use "keys". Remove debug prints at some point.

    if(Config.eventDebug) {println("worldstate: " + worldState)}

    // non state-specific events
    globalEvents.foreach{
      case Push(state) => push(state)
      case Pop => pop()
    }
    globalEvents = List.empty

    // update for all existing states
    worldState.keys.foreach(_.idleUpdate(this, deltaTime))

    // update for active state
    worldState.keys.head.update(this, deltaTime)

    // event handling for all states
    worldState.foreach { case (state, events) =>
      if(Config.eventDebug) {println("state: " + state)}
      val pf = state.default orElse state.mutate
      events.reverse.foreach(e => {
        if(Config.eventDebug) {println("event: " + e)}
        if (pf.isDefinedAt(e)) {
          pf(e)(this, state)
        }
        worldState = worldState.updated(state, List.empty)
      })
    }
  }

  def render(deltaTime: Long): Unit = {
    worldState.keys.toList.reverse.foreach(_.render(this, deltaTime))
  }


}
