package states

import components.{BackgroundColor, Position}
import drawables.{Color, Text}
import engine.Event.Identity
import engine.GlobalEvent.Pop
import engine.{Drawable, Entity, Event, State, World}
import render.Grid
import systems.{DrawMonocolorBackground, ImmediateMovement, Input, ScrollMovement}
import util.InputItem.{Back, UpArrow}
import util.{InputItem, Vector2}

class MainMenu(state: State) extends State {

  override protected var _grid: Grid = new Grid(Vector2(0.25f, 1), Vector2(0.75f,0))

  override def init(): Unit = {
    backgroundEntity()
    cursorEntity()
  }

  override def mutate: PartialFunction[Event, Mutation] = {case Identity => (w,s) =>}

  override protected def inputPressed: PartialFunction[InputItem, Mutation] = {
    case Back => (w,s) =>
      w.emit(Pop)
  }

  override def update(world: World, deltaTime: Long): Unit = {
    Input.update(world, this, deltaTime)
  }

  override def render(): Unit = {
    DrawMonocolorBackground.render(this)
  }

  private def backgroundEntity(): Unit = {
    val e = new Entity {}
    e.attach(BackgroundColor(Color.Blue.translucent(0.75f)))
    entities = e +: entities
  }

  private def cursorEntity(): Unit = {
    val e = new Entity {}
    e.attach(Position(Vector2(0)))
    entities = e +: entities
  }
}

object MainMenu {

}
