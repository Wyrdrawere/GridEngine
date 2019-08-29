package states

import components.{Background, BackgroundColor, Cursor, ListMenu, Position}
import drawables.{Color, Text}
import engine.Event.{ChangeMenu, Identity, InitMove, Move}
import engine.GlobalEvent.{Pop, Push}
import engine.{Drawable, Entity, Event, State, World}
import render.Grid
import systems.{DrawListMenu, DrawMonocolorBackground, ImmediateMovement, Input, ScrollMovement}
import util.InputItem.{Back, DownArrow, Enter, UpArrow}
import util.{InputItem, Vector2}

class MainMenu(affectedState: State) extends State {

  override protected var _grid: Grid = new Grid(Vector2(0.25f, 1), Vector2(0.75f,0))

  override def init(): Unit = {
    backgroundEntity()
    cursorEntity()
    menuEntity()
  }

  private def maxC: Int = selectEntities(ListMenu).head.get(ListMenu) match {
    case Some(v) => v.items.length-1
    case None => 0
  }

  override def mutate: PartialFunction[Event, Mutation] = {
    case ChangeMenu => (w,s) => w.emit(Push(new JobMenu(affectedState)))
  }

  override protected def inputPressed: PartialFunction[InputItem, Mutation] = {
    case Back => &(List(
      (w,s) => w.emit(Pop),
      Input.delay(Back, 500)
    ))
    case Enter => &(List(
      (w,s) => {
        selectEntities(Cursor).foreach(_.get(Position).foreach(p => {
          selectEntities(ListMenu).foreach(_.get(ListMenu).foreach(ls => {
            ls.items(p.value.yi)._2 match {
              case ChangeMenu => w.emit(Push(new JobMenu(affectedState)))
              case e@_ => w.emit(e, affectedState)
            }

          }))
        }))
      }
    ))
    case UpArrow => &(List(
      ImmediateMovement.Move(selectEntities(Cursor).head, Vector2.Down, Vector2(0), Vector2(0, maxC)),
      Input.delay(UpArrow, 300)))
    case DownArrow => &(List(
      ImmediateMovement.Move(selectEntities(Cursor).head, Vector2.Up, Vector2(0), Vector2(0,maxC)),
      Input.delay(DownArrow, 300)))
  }

  override def idleUpdate(world: World, deltaTime: Long): Unit = {
  }

  override def update(world: World, deltaTime: Long): Unit = {
    Input.update(world, this, deltaTime)
  }

  override def render(world: World, deltaTime: Long): Unit = {
    DrawMonocolorBackground.update(world, this, deltaTime)
    DrawListMenu.update(world, this, deltaTime)
  }

  private def backgroundEntity(): Unit = {
    val e = new Entity {}
    e.attach(BackgroundColor(Color.Blue.translucent(0.75f)))
    entities = e +: entities
  }

  private def cursorEntity(): Unit = {
    val e = new Entity {}
    e.attach(Position(Vector2(0)))
    e.attach(Cursor(Color.Pink))
    entities = e +: entities
  }

  private def menuEntity(): Unit = {
    val e = new Entity {}
    e.attach(ListMenu(List(
      (Text("UP", Text.WhiteFont), InitMove(affectedState.selectEntities(Background).head, Vector2.Up)),
      (Text("DOWN", Text.WhiteFont), InitMove(affectedState.selectEntities(Background).head, Vector2.Down)),
      (Text("LEFT", Text.WhiteFont), InitMove(affectedState.selectEntities(Background).head, Vector2.Left)),
      (Text("RIGHT", Text.WhiteFont), InitMove(affectedState.selectEntities(Background).head, Vector2.Right)),
      (Text("JOBS", Text.WhiteFont), ChangeMenu)
    )))
    entities = e +: entities
  }
}

object MainMenu {

}
