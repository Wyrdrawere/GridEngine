package engine

import render.Grid

import scala.collection.View

trait State {

  type Mutation = NewWorld => Unit

  final protected def &(ms: List[Mutation]): Mutation = {
    w => ms.foreach(_(w))
  }



  protected var entities: List[NewEntity] = List.empty
  protected var _grid: Grid

  def grid: Grid = _grid

  final def allEntities: List[NewEntity] = entities

  final def selectEntities[C <: Component](componentKey: ComponentKey[C]): View[NewEntity] = entities.view.filter(_.has(componentKey))

  def init(): Unit

  def mutate: PartialFunction[Event, Mutation]

  def update(newWorld: NewWorld, deltaTime: Long): Unit

  def render(newWorld: NewWorld, deltaTime: Long): Unit

}
