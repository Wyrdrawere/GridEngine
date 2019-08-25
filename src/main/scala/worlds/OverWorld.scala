package worlds

import components.{Background, Position, Scroll}
import drawables.Sprite
import engine.Event.{KeyPressed, Move}
import engine.{Entity, World}
import systems.{Input, LevelDraw, ScrollMovement}
import util.InputItem.{DownArrow, K, LeftArrow, Num1, Num2, Num3, Num4, Num5, Num6, Num7, Num8, Num9, RightArrow, UpArrow}
import util.{Config, InputItem, Vector2}

class OverWorld extends World{

  register(inputListener)
  register(actionListener)

  def inputListener: Listener = {
    case KeyPressed(key) => key match {
      case UpArrow => &(initMove(Vector2.Up), delay(UpArrow, 500))
      case DownArrow => &(initMove(Vector2.Down), delay(DownArrow, 500))
      case LeftArrow => &(initMove(Vector2.Left), delay(LeftArrow, 500))
      case RightArrow => &(initMove(Vector2.Right), delay(RightArrow, 500))
      case Num1 => initMove(Vector2.Left + Vector2.Down)
      case Num2 => initMove(Vector2.Down)
      case Num3 => initMove(Vector2.Right + Vector2.Down)
      case Num4 => initMove(Vector2.Left)
      case Num6 => initMove(Vector2.Right)
      case Num7 => initMove(Vector2.Left + Vector2.Up)
      case Num8 => initMove(Vector2.Up)
      case Num9 => initMove(Vector2.Right + Vector2.Up)
      case _ => (l,w) =>
    }
  }

  def actionListener: Listener = {
    case Move(entity, dir) => move(entity, dir)
  }

  private def initMove(dir: Vector2): Mutation = {
    (_, w: World) =>
      w.selectEntities(Position).view.foreach(e => {
        if (!e.has(Scroll)) {
          e.attach(Scroll(0, Config.scrollUnit, dir))
        }
      })
  }

  private def move(entity: Entity, direction: Vector2): Mutation = {
    (l,w) =>
      val e: Unit = entity.modify[Position](p => Position(p.value+direction))
      println(entity.get(Position))
      e
  }

  private def delay(input: InputItem, deltaTime: Long): Mutation = {
    (_,_) => Input.delay(input, deltaTime)
  }

  def levelEntity(): Unit = {
    val e = new Entity {}
    e.attach(Position(Vector2(0)))
    e.attach(OverWorld.dungeonBackground)
    entities = e +: entities
  }

  override protected def systemsUpdate(deltaTime: Long): Unit = {
    Input.update(this, deltaTime)
    ScrollMovement.update(this, deltaTime)
    LevelDraw.update(this, deltaTime)
  }
}

object OverWorld {

  lazy val dungeonBackground: Background = {
    val bg = Array.ofDim[Int](21, 11)
    for (x <- bg.indices; y <- bg(x).indices) {
      if(y == bg(x).length-1) {
        if(x % 2 == 0) {
          bg(x)(y) = 0
        } else {
          bg(x)(y) = 2
        }
      } else {
        bg(x)(y) = 1
      }
    }
    lazy val sheet = Sprite.get("src/resources/Tileset/basictiles.png", Vector2(128,240), Vector2(16))
    Background(bg, sheet, 22)
  }

}
