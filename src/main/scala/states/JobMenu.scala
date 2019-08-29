package states

import components.{Background, BackgroundColor, Cursor, PanelMenu, Position}
import drawables.{Color, Sprite, Text}
import engine.Event.Identity
import engine.GlobalEvent.Pop
import engine.{Entity, Event, State, World}
import render.Grid
import systems.{DrawMonocolorBackground, ImmediateMovement, Input}
import util.InputItem.{Back, DownArrow, LeftArrow, UpArrow}
import util.{InputItem, Vector2}

class JobMenu(affectedState: State) extends State {
  override protected var _grid: Grid = new Grid(Vector2(0.75f), Vector2(0,0.25f))

  override def init(): Unit = {
    backgroundEntity()
    cursorEntity()
    menuEntity()
  }

  override def mutate: PartialFunction[Event, Mutation] = {
    case Identity => (w,s) =>
  }

  override protected def inputPressed: PartialFunction[InputItem, Mutation] = {
    case Back => &(List(
      (w,s) => w.emit(Pop),
      Input.delay(Back, 500)
    ))
    case UpArrow => &(List(

      Input.delay(UpArrow, 300)))
    case DownArrow => &(List(

      Input.delay(DownArrow, 300)))
    case LeftArrow => &(List(

      Input.delay(UpArrow, 300)))
    case UpArrow => &(List(

      Input.delay(DownArrow, 300)))
  }

  override def update(world: World, deltaTime: Long): Unit = {
    Input.update(world, this, deltaTime)
  }



  override def render(world: World, deltaTime: Long): Unit = {
    DrawMonocolorBackground.update(world, this, deltaTime)
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
    e.attach(Position(Vector2(0)))

    e.attach(PanelMenu())
    entities = e +: entities
  }

}

object JobMenu {

  def spriteSheetConvert(sprites: Map[Int, Sprite], cols: Int): Array[Array[Sprite]] = {
    var s: List[Int] = sprites.keys.filter(x => x % cols == 0).toList.sorted

    val a = Array.ofDim[Int](Math.ceil(s.length/2).toInt,2)

    for(x <- a.indices; y <- a(x).indices) {
      a(x)(y) = s.head
      s = s.tail
    }

  }

}
