package engine

import engine.Event.{KeyPressed, KeyReleased, MousePressed, MouseReleased, RemoveComponent}
import render.Grid
import util.InputItem

import scala.collection.View

trait State {

  type Mutation = (World, State) => Unit

  final protected def &(ms: List[Mutation]): Mutation = { // todo: make this work without List
    (w,s) => ms.foreach(_(w,s))
  }

  protected var entities: List[Entity] = List.empty
  protected var _grid: Grid

  def grid: Grid = _grid // todo: figure out exact usage/need of grid and change this accordingly

  final def allEntities: List[Entity] = entities

  final def selectEntities[C <: Component](componentKey: ComponentKey[C]): View[Entity] = {
    // todo: add functionality to select entities based on AND, OR and NOT
    entities.view.filter(_.has(componentKey))
  }

  final def default: PartialFunction[Event, Mutation] = {
    case RemoveComponent(entity, componentKey) => (w,s) => entity.detach(componentKey)
    case KeyPressed(key) if inputPressed.isDefinedAt(key) => inputPressed(key)
    case MousePressed(button) if inputPressed.isDefinedAt(button) => inputPressed(button)
    case KeyReleased(key) if inputReleased.isDefinedAt(key) => inputReleased(key)
    case MouseReleased(button) if inputReleased.isDefinedAt(button) => inputReleased(button)
  }

  protected def inputPressed: PartialFunction[InputItem, Mutation] = {case _ => (w,s) =>}

  protected def inputReleased: PartialFunction[InputItem, Mutation] = {case _ => (w,s) =>}

  def init(): Unit

  def mutate: PartialFunction[Event, Mutation]

  def update(world: World, deltaTime: Long): Unit

  def idleUpdate(world: World, deltaTime: Long): Unit = ()

  def render(world: World, deltaTime: Long): Unit

}
