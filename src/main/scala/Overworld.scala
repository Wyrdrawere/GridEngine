import Mutation.{ChangeColorMut, Direction, DownMut, Identity, LeftMut, MakeSubMenu, OpenMenuMut, PauseMut, RightMut, SetChild, SetReturnMutation, UpMut}
import Scroller.{Rest, ScrollX, ScrollY, Stay}

case class Overworld
(
  level: Level,
  playerSprite: OverworldSprite,
  pos: Vector2,
  zoom: Int,
  scroller: Scroller,

  grid: Grid,
  childState: Option[Stateful] = None,
  returnMutation: Mutation = Identity
) extends Stateful {

  override def everyFrame(deltaTime: Long): Stateful = {
    val newScroller = scroller.increment //todo: too hacky, please fix
    val newPos = if (scroller.increment.currentScroll == Rest) pos+scroller.scrollDirection else pos
    this.copy(pos = newPos, scroller = newScroller)
  }

  override def mutate(mutation: Mutation): Stateful = mutation match {
    case Identity => this
    case Direction(dir) if scroller.currentScroll == Scroller.Stay =>
      this.copy(playerSprite = playerSprite.animateSprite(OverworldSprite.Walk(dir)), scroller = scroller(dir))
    case PauseMut => this.copy(childState = Some(makeMenu()))
    case SetChild(state) => this.copy(childState = state)
    case _ => this
  }

  override def draw(grid: Grid): Unit = {
    grid.drawGrid(level.getSlice(Vector2(zoom*2+1), pos), level.tileSet, scroller.toVector2/scroller.scrollUnit)
    grid.drawOnCenter(playerSprite)
  }

  private def makeMenu(): ListMenu = {
    val items = Map(
      0 -> (Text("Up", Text.DarkGrayFont), SetReturnMutation(Direction(Vector2.Up))),
      1 -> (Text("Down", Text.DarkGrayFont), SetReturnMutation(Direction(Vector2.Down))),
      2 -> (Text("Left", Text.DarkGrayFont), SetReturnMutation(Direction(Vector2.Left))),
      3 -> (Text("Right", Text.DarkGrayFont), SetReturnMutation(Direction(Vector2.Right))),
      4 -> (Text("Jobs", Text.DarkGrayFont), MakeSubMenu),
    )
    val subgrid = new Grid(Vector2(0.25f, 0.75f), relativePosition = Vector2(0.75f, 0.25f))
    ListMenu(0, items, subgrid)
  }


}
