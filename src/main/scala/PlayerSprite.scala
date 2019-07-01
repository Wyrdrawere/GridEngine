import Input.{DownArrow, LeftArrow, RightArrow, UpArrow}
import PlayerSprite.{SpriteMode, WalkDown, WalkLeft, WalkRight, WalkUp}

case class PlayerSprite
(
  spriteSheet: Map[Int, Drawable],
  modeMap: Map[SpriteMode, List[Int]],
  currentSprite: Int,

) extends Stateful {

  override def simulate(deltaTime: Long, input: Input): PlayerSprite = input match {
    case UpArrow => switchSprite(WalkUp)
    case DownArrow => switchSprite(WalkDown)
    case LeftArrow => switchSprite(WalkLeft)
    case RightArrow => switchSprite(WalkRight)
    case _ => this
  }

  override def render(grid: Grid): Unit = grid.drawOnCenter(spriteSheet(currentSprite))

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
    Sprite.TextureToTileSet(Texture.load("src/resources/Sprite/ff1-classes.png"), 972, 432, 36, 36),
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
