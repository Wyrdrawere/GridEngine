package states

import components.{AnimatedSprite, Background, Player, Position, Scroll, Zoom}
import drawables.Sprite
import engine.Event.{InitMove, KeyPressed, Move, RemoveComponent}
import engine.GlobalEvent.Push
import engine.{Entity, Event, State, World}
import render.Grid
import systems.SpriteAnimation.Walk
import systems.{DrawSprite, DrawTiledBackground, ImmediateMovement, Input, ScrollMovement, SpriteAnimation}
import util.InputItem.{A, DownArrow, LeftArrow, Num1, Num2, Num3, Num4, Num5, Num6, Num7, Num8, Num9, RightArrow, S, Space, UpArrow, W}
import util.{InputItem, Vector2}

class Overworld extends State {

  override protected var _grid: Grid = new Grid()

  override def init(): Unit = {
    levelEntity()
    playerEntity(0)
  }

  override def mutate: PartialFunction[Event, Mutation] = {
    case Move(entity, direction) => ScrollMovement.move(entity, direction)
    case InitMove(entity, direction) => &(List(
      SpriteAnimation.animatePlayer(Walk(direction)),
      ScrollMovement.initMove(entity, direction)
      ))
  }

  override protected def inputPressed: PartialFunction[InputItem, Mutation] = {
    case A => &(List(
      ImmediateMovement.Move(selectEntities(Background).head, Vector2.Left),
      Input.delay(A, 500)))
    case UpArrow => &(List(
      mutate(InitMove(selectEntities(Background).head, Vector2.Up)),
      Input.delay(UpArrow, 500)))
    case DownArrow => &(List(
      mutate(InitMove(selectEntities(Background).head, Vector2.Down)),
      Input.delay(DownArrow, 500)))
    case LeftArrow => &(List(
      mutate(InitMove(selectEntities(Background).head, Vector2.Left)),
      Input.delay(LeftArrow, 500)))
    case RightArrow => &(List(
      mutate(InitMove(selectEntities(Background).head, Vector2.Right)),
      Input.delay(RightArrow, 500)))
    case Num1 => mutate(InitMove(selectEntities(Background).head, Vector2.Down+Vector2.Left))
    case Num2 => &(List(
      mutate(InitMove(selectEntities(Background).head, Vector2.Down))))
    case Num3 => mutate(InitMove(selectEntities(Background).head, Vector2.Down+Vector2.Right))
    case Num4 => &(List(
      mutate(InitMove(selectEntities(Background).head, Vector2.Left))))
    case Num6 => &(List(
      mutate(InitMove(selectEntities(Background).head, Vector2.Right))))
    case Num7 => mutate(InitMove(selectEntities(Background).head, Vector2.Up+Vector2.Left))
    case Num8 => &(List(
      mutate(InitMove(selectEntities(Background).head, Vector2.Up))))
    case Num9 => mutate(InitMove(selectEntities(Background).head, Vector2.Up+Vector2.Right))
    case Space => (w,s) => w.emit(Push(new MainMenu(this)))
  }

  override def idleUpdate(world: World, deltaTime: Long): Unit = {
    ScrollMovement.update(world, this, deltaTime)
  }

  override def update(world: World, deltaTime: Long): Unit = {
    Input.update(world, this, deltaTime)
  }

  override def render(world: World, deltaTime: Long): Unit = {
    DrawTiledBackground.update(world, this, deltaTime)
    DrawSprite.update(world, this, deltaTime)
  }

  //todo: (at some point) Entityfactory, these defs dont belong here
  private def levelEntity(): Unit = {
    val e = new Entity {}
    e.attach(Position(Vector2(0)))
    e.attach(Overworld.dungeonBackground)
    e.attach(Zoom(5))
    entities = e +: entities
  }

  private def playerEntity(job: Int): Unit = {
    val e = new Entity {}
    e.attach(Player())
    e.attach(Overworld.playerSprite(job))
    entities = e +: entities
  }

}

object Overworld {

  def dungeonBackground: Background = {
    val bg = Array.ofDim[Int](21, 11)
    for (x <- bg.indices; y <- bg(x).indices) {
      if (y == bg(x).length - 1) {
        if (x % 2 == 0) {
          bg(x)(y) = 0
        } else {
          bg(x)(y) = 2
        }
      } else {
        bg(x)(y) = 1
      }
    }
    val sheet = Sprite.get("src/resources/Tileset/basictiles.png", Vector2(128, 240), Vector2(16))
    Background(bg, sheet, 22)
  }

  def playerSprite(job: Int): AnimatedSprite = {
    AnimatedSprite(
      Sprite.get("src/resources/Sprite/ff1-classes.png", Vector2(972, 432), Vector2(36, 36)),
      Map(
        Walk(Vector2.Up) -> List(27 * job + 2, 27 * job + 3),
        Walk(Vector2.Down) -> List(27 * job, 27 * job + 1),
        Walk(Vector2.Left) -> List(27 * job + 6, 27 * job + 7),
        Walk(Vector2.Right) -> List(27 * job + 4, 27 * job + 5)
      ),
      27 * job
    )
  }

}