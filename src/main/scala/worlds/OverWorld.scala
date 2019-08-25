package worlds

import components.{AnimatedSprite, Background, Player, Position, Scroll, Zoom}
import deprecate.{DrawLevel, DrawSprite, Entity, Input, ScrollMovement, SpriteAnimation, World, ZoomSystem}
import drawables.Sprite
import engine.Event.{KeyPressed, Move}
import deprecate.SpriteAnimation.{SpriteMode, Walk}
import util.InputItem.{DownArrow, K, LeftArrow, Num1, Num2, Num3, Num4, Num5, Num6, Num7, Num8, Num9, RightArrow, S, UpArrow, W}
import util.{Config, InputItem, Vector2}

class OverWorld extends World{

  register(inputListener)
  register(actionListener)

  private def inputListener: Listener = {
    case KeyPressed(key) => key match {
      case W => &(List(
        ZoomSystem.zoom(-1),
        Input.delay(W,300)))
      case S => &(List(
        ZoomSystem.zoom(1),
        Input.delay(S,300)))
      case UpArrow => &(List(
        SpriteAnimation.animatePlayer(Walk(Vector2.Up)),
        ScrollMovement.initMove(Vector2.Up),
        Input.delay(UpArrow, 500)))
      case DownArrow => &(List(
        SpriteAnimation.animatePlayer(Walk(Vector2.Down)),
        ScrollMovement.initMove(Vector2.Down),
        Input.delay(DownArrow, 500)))
      case LeftArrow => &(List(
        SpriteAnimation.animatePlayer(Walk(Vector2.Left)),
        ScrollMovement.initMove(Vector2.Left),
        Input.delay(LeftArrow, 500)))
      case RightArrow => &(List(
        SpriteAnimation.animatePlayer(Walk(Vector2.Right)),
        ScrollMovement.initMove(Vector2.Right),
        Input.delay(RightArrow, 500)))
      case Num1 => ScrollMovement.initMove(Vector2.Left + Vector2.Down)
      case Num2 => &(List(
        SpriteAnimation.animatePlayer(Walk(Vector2.Down)),
        ScrollMovement.initMove(Vector2.Down)))
      case Num3 => ScrollMovement.initMove(Vector2.Right + Vector2.Down)
      case Num4 => &(List(
        SpriteAnimation.animatePlayer(Walk(Vector2.Left)),
        ScrollMovement.initMove(Vector2.Left)))
      case Num5 => &(List(
        (l,w) => selectEntities(Background).view.foreach(b => b.modify[Position](p => if(!b.has(Scroll)) Position(Vector2(0)) else p)),
        Input.delay(Num5, 500)))
      case Num6 => &(List(
        SpriteAnimation.animatePlayer(Walk(Vector2.Right)),
        ScrollMovement.initMove(Vector2.Right)))
      case Num7 => ScrollMovement.initMove(Vector2.Left + Vector2.Up)
      case Num8 => &(List(
        SpriteAnimation.animatePlayer(Walk(Vector2.Up)),
        ScrollMovement.initMove(Vector2.Up)))
      case Num9 => ScrollMovement.initMove(Vector2.Right + Vector2.Up)
      case _ => (l,w) =>
    }
  }

  private def actionListener: Listener = {
    case Move(entity, dir) => ScrollMovement.move(entity, dir)
  }

  override protected def systemsUpdate(deltaTime: Long): Unit = {
    Input.update(this, deltaTime)
    ScrollMovement.update(this, deltaTime)
    DrawLevel.update(this, deltaTime)
    DrawSprite.update(this, deltaTime)
  }

  def levelEntity(): Unit = {
    val e = new Entity {}
    e.attach(Position(Vector2(0)))
    e.attach(OverWorld.dungeonBackground)
    e.attach(Zoom(5))
    entities = e +: entities
  }

  def playerEntity(job: Int): Unit = {
    val e = new Entity {}
    e.attach(Player())
    e.attach(AnimatedSprite(
      Sprite.get("src/resources/Sprite/ff1-classes.png", Vector2(972, 432), Vector2(36, 36)),
      Map(
        Walk(Vector2.Up) -> List(27*job+2, 27*job+3),
        Walk(Vector2.Down) -> List(27*job, 27*job+1),
        Walk(Vector2.Left) -> List(27*job+6, 27*job+7),
        Walk(Vector2.Right) -> List(27*job+4, 27*job+5)
      ),
      27*job
    ))
    entities = e +: entities
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
    val sheet = Sprite.get("src/resources/Tileset/basictiles.png", Vector2(128,240), Vector2(16))
    Background(bg, sheet, 22)
  }

}
