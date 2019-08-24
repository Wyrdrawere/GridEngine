package engine

import scala.collection.View

trait World {

  type Listener = PartialFunction[Event, (Long, World) => Unit]

  private def entities: List[Entity] = List.empty
  private var events: List[Event] = List.empty
  private var listeners: List[Listener] = List.empty

  def allEntities: List[Entity] = entities

  def selectEntities[
  C <: Component,
  ](
    componentKey: ComponentKey[C],
  ): View[Entity] = entities.view.filter(_.has(componentKey))

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
