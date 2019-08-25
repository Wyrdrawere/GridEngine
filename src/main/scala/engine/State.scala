package engine

import render.Grid

import scala.collection.View

trait State {

  protected var entities: List[Entity] = List.empty
  protected var grid: Grid

  final def allEntities: List[Entity] = entities

  final def selectEntities[C <: Component](componentKey: ComponentKey[C]): View[Entity] = entities.view.filter(_.has(componentKey))

  def init(): Unit

  def mutate: PartialFunction[Event, NewWorld => Unit]

  def update(deltaTime: Long): Unit

  def render(): Unit

}
