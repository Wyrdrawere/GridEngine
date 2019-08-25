package engine

import engine.Event.RemoveComponent
import render.Grid

import scala.collection.View

trait World {

  val mainGrid: Grid = new Grid()

  type Mutation = (Long, World) => Unit
  type Listener = PartialFunction[Event, Mutation]

  protected var entities: List[Entity] = List.empty
  private var events: List[Event] = List.empty
  private var listeners: List[Listener] = List.empty

  def allEntities: List[Entity] = entities

  def selectEntities[C <: Component](componentKey: ComponentKey[C]): View[Entity] = entities.view.filter(_.has(componentKey))

  def emit(event: Event): Unit = {events = event +: events}

  def register(listener: Listener): Unit = {listeners = listener +: listeners}

  def update(deltaTime: Long): Unit = {
    systemsUpdate(deltaTime)
    val currentEvents = events.reverse
    events = List.empty
    for (event <- currentEvents; listener <- listeners) {
      if(listener.isDefinedAt(event)) {
        listener(event)(deltaTime, this)
      }
    }
  }

  protected def systemsUpdate(deltaTime: Long): Unit

  final protected def &(m1: Mutation, m2: Mutation): Mutation = {
    (l: Long, w: World) =>
      m1(l,w)
      m2(l,w)
  }

  private def debug: Listener = {
    case e@_ => (l: Long, w: World) => println(e)
  }

  private def default: Listener = {
    case RemoveComponent(entity, key) => (l, w) => entity.detach(key)
  }

  register(debug)
  register(default)

}
