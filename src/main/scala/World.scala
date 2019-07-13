import Input.{DownArrow, LeftArrow, RightArrow, UpArrow}
import Mutation.{DownMut, IdentMut, LeftMut, RightMut, UpMut}

case class World
(
  level: Level,
  tileSet: Map[Int, Drawable],
  playerSprite: PlayerSprite,
  pos: (Int,Int),
  zoom: Int,

  grid: Grid,
  parentState: State,
  childState: Option[State] = None,
  returnMutation: Option[Mutation] = None,
) extends State
{
  override def everyFrame(deltaTime: Long): State = this

  override def mutate(mutation: Mutation): State = mutation match {
    case UpMut => this.copy(pos = (pos._1, pos._2 + 1), playerSprite = playerSprite.simulate(0, UpArrow))
    case _ => this
  }

  override def inputToMutation(input: Input): Mutation = input match {
    case UpArrow => UpMut
    case DownArrow => DownMut
    case LeftArrow => LeftMut
    case RightArrow => RightMut
    case _ => IdentMut
  }

  override def draw(): Unit = {
    grid.drawGrid(level.getSlice(zoom*2+1, zoom*2+1, pos), tileSet, Vector2(0))
    playerSprite.render(grid)
  }
}

