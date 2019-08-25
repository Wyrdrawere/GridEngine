package states

import components.{AnimatedSprite, Background, Player, Position, Zoom}
import drawables.Sprite
import engine.{Entity, Event, NewWorld, State}
import render.Grid
import systems.SpriteAnimation.Walk
import util.Vector2

class OverworldState extends State {

  override protected var grid: Grid = _

  override def init(): Unit = {
    levelEntity()
    playerEntity(0)
  }

  override def mutate: PartialFunction[Event, NewWorld => Unit] = {
    case
  }

  override def update(deltaTime: Long): Unit = ???

  override def render(): Unit = ???



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

  def playerSprite(job: Int): AnimatedSprite = {
    AnimatedSprite(
      Sprite.get("src/resources/Sprite/ff1-classes.png", Vector2(972, 432), Vector2(36, 36)),
      Map(
        Walk(Vector2.Up) -> List(27*job+2, 27*job+3),
        Walk(Vector2.Down) -> List(27*job, 27*job+1),
        Walk(Vector2.Left) -> List(27*job+6, 27*job+7),
        Walk(Vector2.Right) -> List(27*job+4, 27*job+5)
      ),
      27*job
    )
  }

}