package states

import components.{AnimatedSprite, Background, Player, Position, Scroll, Zoom}
import drawables.Sprite
import engine.Event.{KeyPressed, NewMove, NewRemoveComponent, RemoveComponent}
import engine.{Event, NewEntity, NewWorld, State}
import render.Grid
import systems.{NewAnim, NewDrawAnim, NewDrawLevel, NewInput, NewScroll}
import deprecate.SpriteAnimation.Walk
import util.InputItem.{DownArrow, LeftArrow, Num1, Num2, Num3, Num4, Num5, Num6, Num7, Num8, Num9, RightArrow, S, UpArrow, W}
import util.Vector2

class OverworldState extends State {

  override protected var _grid: Grid = new Grid

  override def init(): Unit = {
    levelEntity()
    playerEntity(0)
  }

  override def mutate: PartialFunction[Event, NewWorld => Unit] = {
    case NewRemoveComponent(entity, componentKey) => w => entity.detach(componentKey)
    case NewMove(entity, direction) => NewScroll.move(entity, direction)
    case KeyPressed(key) => key match {
      case UpArrow => &(List(
        NewAnim.animatePlayer(Walk(Vector2.Up), this),
        NewScroll.initMove(Vector2.Up, this),
        NewInput.delay(UpArrow, 500)))
      case DownArrow => &(List(
        NewAnim.animatePlayer(Walk(Vector2.Down), this),
        NewScroll.initMove(Vector2.Down, this),
        NewInput.delay(DownArrow, 500)))
      case LeftArrow => &(List(
        NewAnim.animatePlayer(Walk(Vector2.Left), this),
        NewScroll.initMove(Vector2.Left, this),
        NewInput.delay(LeftArrow, 500)))
      case RightArrow => &(List(
        NewAnim.animatePlayer(Walk(Vector2.Right), this),
        NewScroll.initMove(Vector2.Right, this),
        NewInput.delay(RightArrow, 500)))
      case Num1 => NewScroll.initMove(Vector2.Left + Vector2.Down, this)
      case Num2 => &(List(
        NewAnim.animatePlayer(Walk(Vector2.Down), this),
        NewScroll.initMove(Vector2.Down, this)))
      case Num3 => NewScroll.initMove(Vector2.Right + Vector2.Down, this)
      case Num4 => &(List(
        NewAnim.animatePlayer(Walk(Vector2.Left), this),
        NewScroll.initMove(Vector2.Left, this)))
      case Num6 => &(List(
        NewAnim.animatePlayer(Walk(Vector2.Right), this),
        NewScroll.initMove(Vector2.Right, this)))
      case Num7 => NewScroll.initMove(Vector2.Left + Vector2.Up, this)
      case Num8 => &(List(
        NewAnim.animatePlayer(Walk(Vector2.Up), this),
        NewScroll.initMove(Vector2.Up, this)))
      case Num9 => NewScroll.initMove(Vector2.Right + Vector2.Up, this)
      case _ => w =>
    }
    case _ => w =>
  }

  override def update(newWorld: NewWorld, deltaTime: Long): Unit = {
    NewInput.update(newWorld, this, deltaTime)
    NewScroll.update(newWorld, this, deltaTime)
  }

  override def render(newWorld: NewWorld, deltaTime: Long): Unit = {
    NewDrawLevel.renderBackground(this)
    NewDrawAnim.update(newWorld, this, deltaTime)
    println(selectEntities(Scroll).toList)
  }


  private def levelEntity(): Unit = {
    val e = new NewEntity {}
    e.attach(Position(Vector2(0)))
    e.attach(OverworldState.dungeonBackground)
    e.attach(Zoom(5))
    entities = e +: entities
  }

  private def playerEntity(job: Int): Unit = {
    val e = new NewEntity {}
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