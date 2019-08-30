package states

import components.{Background, BackgroundColor, Cursor, PanelMenu, Position}
import drawables.{Color, Sprite, Text}
import engine.Event.{ChangeJob, Identity}
import engine.GlobalEvent.Pop
import engine.{Drawable, Entity, Event, State, World}
import render.Grid
import systems.{DrawMonocolorBackground, DrawPanelMenu, ImmediateMovement, Input}
import util.InputItem.{Back, DownArrow, Enter, LeftArrow, RightArrow, UpArrow}
import util.{InputItem, Vector2}

class JobMenu(affectedState: State) extends State {
  override protected var _grid: Grid = new Grid(Vector2(0.75f, 1), Vector2(0))

  override def init(): Unit = {
    backgroundEntity()
    cursorEntity()
    menuEntity()
    println(maxItems)
  }

  private def maxItems: Vector2 = selectEntities(PanelMenu).head.get(PanelMenu) match {
    case Some(v) => Vector2(v.panels.length - 2, 0)
    case None => Vector2(0)
  }

  private def maxDisplay: Vector2 = Vector2(1)

  override def mutate: PartialFunction[Event, Mutation] = {
    case Identity => (w, s) =>
  }

  override protected def inputPressed: PartialFunction[InputItem, Mutation] = {
    case Back => &(List(
      (w, s) => w.emit(Pop),
      Input.delay(Back, 500)
    ))
    case Enter => &(List(
      (w,s) => {
        selectEntities(PanelMenu).foreach(_.get(PanelMenu).foreach(menu => {
          selectEntities(Cursor).foreach(_.get(Position).foreach(cpos => {
            selectEntities(PanelMenu).foreach(_.get(Position).foreach(mpos => {
              w.emit(menu.panels(cpos.value.xi+mpos.value.xi)(cpos.value.yi)._2, affectedState)
            }))
          }))
        }))
      },
      Input.delay(Enter, 500)
    ))
    case UpArrow => &(List(
      ImmediateMovement.Move(
        selectEntities(Cursor).head,
        Vector2.Up,
        Vector2(0),
        maxDisplay
      ),
      Input.delay(UpArrow, 300)))
    case DownArrow => &(List(
      ImmediateMovement.Move(
        selectEntities(Cursor).head,
        Vector2.Down,
        Vector2(0),
        maxDisplay
      ),
      Input.delay(DownArrow, 300)))
    case LeftArrow => &(List(
      {
        var e: Mutation = (w,s) => ()
        selectEntities(PanelMenu).foreach(_.get(Position).foreach(mpos => {
          selectEntities(Cursor).foreach(_.get(Position).foreach(cpos => {
            if (cpos.value.x == maxDisplay.x) {
              e = ImmediateMovement.Move(
                selectEntities(Cursor).head,
                Vector2.Left,
                Vector2(0),
                maxDisplay
              )
            } else {
              e = ImmediateMovement.Move(
                selectEntities(PanelMenu).head,
                Vector2.Left,
                Vector2(0),
                maxItems
              )
            }
          }))
        }))
        e
      },
      Input.delay(LeftArrow, 300)))
    case RightArrow => &(List(
      {
        var e: Mutation = (w,s) => ()
        selectEntities(PanelMenu).foreach(_.get(Position).foreach(mpos => {
          selectEntities(Cursor).foreach(_.get(Position).foreach(cpos => {
            if (cpos.value.x == maxDisplay.x) {
              e = ImmediateMovement.Move(
                selectEntities(PanelMenu).head,
                Vector2.Right,
                Vector2(0),
                maxItems
              )
            } else {
              e = ImmediateMovement.Move(
                selectEntities(Cursor).head,
                Vector2.Right,
                Vector2(0),
                maxDisplay
              )
            }
          }))
        }))
        e
      },
      Input.delay(RightArrow, 300)))
  }

  override def update(world: World, deltaTime: Long): Unit = {
    Input.update(world, this, deltaTime)
  }

  override def render(world: World, deltaTime: Long): Unit = {
    DrawMonocolorBackground.update(world, this, deltaTime)
    DrawPanelMenu.update(world, this, deltaTime)
  }

  private def backgroundEntity(): Unit = {
    val e = new Entity {}
    e.attach(BackgroundColor(Color.Blue.translucent(0.75f)))
    entities = e +: entities
  }

  private def cursorEntity(): Unit = {
    val e = new Entity {}
    e.attach(Position(Vector2(0,1)))
    e.attach(Cursor(Color.Pink))
    entities = e +: entities
  }

  private def menuEntity(): Unit = {
    val e = new Entity {}
    e.attach(Position(Vector2(0)))
    e.attach(PanelMenu(JobMenu.spriteSheetConvert(Sprite.ff1_Spritesheet, 27)))
    entities = e +: entities
  }

}

object JobMenu {

  def spriteSheetConvert(sprites: Map[Int, Sprite], cols: Int): Array[Array[(Drawable, Event)]] = {
    var s: List[Int] = sprites.keys.filter(x => x % cols == 0).toList.sorted
    val a = Array.ofDim[Int](Math.ceil(s.length / 2).toInt, 2)
    val d = Array.ofDim[(Drawable, Event)](Math.ceil(s.length / 2).toInt, 2)

    for (x <- a.indices; y <- a(x).indices) {
      if (s.nonEmpty) {
        a(x)(y) = s.head
        s = s.tail
      } else {
        a(x)(y) = -1
      }
    }
    val b = a.map(x => x.reverse.map {
      case -1 => Color.Blue.translucent(0.75f)
      case i@_ => sprites(i)
    })
    val c = a.map(x => x.reverse.map {
      case -1 => Identity
      case i@_ => ChangeJob(i/27)
    })


    for (x <- d.indices; y <- d(x).indices) {
      d(x)(y) = (b(x)(y).asInstanceOf[Drawable], c(x)(y).asInstanceOf[Event])
    }
    d
  }

}
