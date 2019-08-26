package states

import components.{AnimatedSprite, Background, Player, Position, Scroll, Zoom}
import drawables.Sprite
import engine.Event.{KeyPressed, Move, RemoveComponent}
import engine.{Event, Entity, World, State}
import render.Grid
import systems.SpriteAnimation.Walk
import systems.{SpriteAnimation, DrawSprite, DrawBackground, Input, ScrollMovement}
import util.InputItem.{DownArrow, LeftArrow, Num1, Num2, Num3, Num4, Num5, Num6, Num7, Num8, Num9, RightArrow, S, UpArrow, W}
import util.Vector2

class OverworldState extends State {

  override protected var _grid: Grid = new Grid(Vector2(0.5f), Vector2(0.25f))

  override def init(): Unit = {
    levelEntity()
    playerEntity(0)
  }

  override def mutate: PartialFunction[Event, Mutation] = {
    case RemoveComponent(entity, componentKey) => (w,s) => entity.detach(componentKey)
    case Move(entity, direction) => ScrollMovement.move(entity, direction)
    case KeyPressed(key) => key match {
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
      case Num6 => &(List(
        SpriteAnimation.animatePlayer(Walk(Vector2.Right)),
        ScrollMovement.initMove(Vector2.Right)))
      case Num7 => ScrollMovement.initMove(Vector2.Left + Vector2.Up)
      case Num8 => &(List(
        SpriteAnimation.animatePlayer(Walk(Vector2.Up)),
        ScrollMovement.initMove(Vector2.Up)))
      case Num9 => ScrollMovement.initMove(Vector2.Right + Vector2.Up)
      case _ => (w,s) =>
    }
    case _ => (w,s) =>
  }

  override def update(newWorld: World, deltaTime: Long): Unit = {
    Input.update(newWorld, this, deltaTime)
    ScrollMovement.update(newWorld, this, deltaTime)
  }

  override def render(): Unit = {
    DrawBackground.renderBackground(this)
    DrawSprite.renderSprite(this)
  }


  private def levelEntity(): Unit = {
    val e = new Entity {}
    e.attach(Position(Vector2(0)))
    e.attach(OverworldState.dungeonBackground)
    e.attach(Zoom(5))
    entities = e +: entities
  }

  private def playerEntity(job: Int): Unit = {
    val e = new Entity {}
    e.attach(Player())
    e.attach(OverworldState.playerSprite(job))
    entities = e +: entities
  }

}

object OverworldState {

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