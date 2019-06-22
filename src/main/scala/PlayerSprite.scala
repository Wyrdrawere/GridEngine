import Input.{Down, Left, Right, Up}
import PlayerSprite.{SpriteMode, WalkDown, WalkLeft, WalkRight, WalkUp}
import Tile.Sprite

case class PlayerSprite
(
  spriteSheet: Map[Int, Sprite],
  modeMap: Map[SpriteMode, List[Int]],
  currentSprite: Int,

) extends Stateful {

  override def simulate(deltaTime: Long, input: Input): PlayerSprite = input match {
    case Up => switchSprite(WalkUp)
    case Down => switchSprite(WalkDown)
    case Left => switchSprite(WalkLeft)
    case Right => switchSprite(WalkRight)
    case _ => this
  }

  override def render(): Unit = Render.centerSprite(spriteSheet(currentSprite))

  private def switchSprite(mode: SpriteMode): PlayerSprite = {
    val nextMode = modeMap(mode)
    var nextSprite = nextMode.head
    if (nextMode.contains(currentSprite)) {
      val nextId = nextMode.indexOf(currentSprite) + 1
      if (nextMode.indices.contains(nextId)) {
        nextSprite = nextMode(nextId)
      }
    }
    this.copy(currentSprite = nextSprite)
  }
}

object PlayerSprite {
  def FF1_PlayerSprite(job: Int) = PlayerSprite(
    Tile.Sprite.TextureToTileSet(TextureLoad("src/resources/Sprite/ff1-classes.png"), 972, 432, 27, 12),
    Map(
      WalkUp -> List(27*job+2, 27*job+3),
      WalkDown -> List(27*job, 27*job+1),
      WalkLeft -> List(27*job+4, 27*job+5),
      WalkRight -> List(27*job+6, 27*job+7)
    ),
    27*job
  )

  sealed trait SpriteMode
  case object WalkUp extends SpriteMode
  case object WalkDown extends SpriteMode
  case object WalkLeft extends SpriteMode
  case object WalkRight extends SpriteMode
}
