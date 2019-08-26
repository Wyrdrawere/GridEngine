package engine

import render.Grid

import scala.collection.View

trait State {

  type Mutation = (World, State) => Unit

  final protected def &(ms: List[Mutation]): Mutation = {
    (w,s) => ms.foreach(_(w,s))
  }

  protected var entities: List[Entity] = List.empty
  protected var _grid: Grid

  def grid: Grid = _grid

  final def allEntities: List[Entity] = entities

  final def selectEntities[C <: Component](componentKey: ComponentKey[C]): View[Entity] = entities.view.filter(_.has(componentKey))

  def init(): Unit

  def mutate: PartialFunction[Event, Mutation]

  def update(newWorld: World, deltaTime: Long): Unit

  def render(): Unit

}
