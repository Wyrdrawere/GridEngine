import Input.{DownArrow, LeftArrow, RightArrow, Space, UpArrow}
import Mutation.{ChangeColorMut, DownMut, IdentMut, LeftMut, OpenMenuMut, RemoveChildMut, RightMut, UpMut}

case class World
(
  level: Level,
  tileSet: Map[Int, Drawable],
  playerSprite: Drawable,
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
    case UpMut => this.copy(pos = (pos._1, pos._2 + 1))
    case DownMut => this.copy(pos = (pos._1, pos._2 - 1))
    case LeftMut => this.copy(pos = (pos._1 - 1, pos._2))
    case RightMut => this.copy(pos = (pos._1 + 1, pos._2))
    case ChangeColorMut(color) => this.copy(playerSprite = color)
    case OpenMenuMut => this.copy(childState = Some(makeMenu()))
    case RemoveChildMut => this.copy(childState = None)
    case _ => this
  }

  override def inputToMutation(input: Input): Mutation = input match {
    case Space => OpenMenuMut
    case _ => IdentMut
  }

  override def draw(): Unit = {
    grid.drawGrid(level.getSlice(zoom*2+1, zoom*2+1, pos), tileSet, Vector2(0))
    grid.drawOnCenter(playerSprite)
  }

  private def makeMenu(): ListMenuState = {
    val items = Map(
      0 -> (Text("Up", Text.DarkGrayFont), UpMut),
      1 -> (Text("Down", Text.DarkGrayFont), DownMut),
      2 -> (Text("Left", Text.DarkGrayFont), LeftMut),
      3 -> (Text("Right", Text.DarkGrayFont), RightMut),
      4 -> (Text("Blue", Text.DarkGrayFont), ChangeColorMut(Color.Blue)),
      5 -> (Text("Green", Text.DarkGrayFont), ChangeColorMut(Color.Green)),
      6 -> (Text("Red", Text.DarkGrayFont), ChangeColorMut(Color.Red)),
    )
    val subgrid = new Grid(Vector2(0.25f, 1), relativePosition = Vector2(0.75f, 0))
    ListMenuState(0, items, subgrid, this)
  }

  override def updateChild(child: State): State = this.copy(childState = Some(child))
}

