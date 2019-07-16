import Mutation.{ChangeColorMut, ChangeJob, Direction, DownMut, Identity, LeftMut, MakeSubMenu, OpenMenuMut, PauseMut, RightMut, SetChild, SetReturnMutation, UpMut}
import Scroller.{Rest, ScrollX, ScrollY, Stay}
import Stateful.Receive

class Overworld
(
  override val box: Statebox.OverworldBox,
  override val grid: Grid,
  override val childState: Option[Stateful] = None,
  override val returnMutation: Mutation = Identity
) extends Stateful {

  override def copy
  (box: Statebox = box,
   grid: Grid = grid,
   childState: Option[Stateful] = childState,
   returnMutation: Mutation = returnMutation): Stateful = new Overworld(
    box.asInstanceOf[Statebox.OverworldBox], grid, childState, returnMutation
  )

  override def everyFrame(deltaTime: Long): Stateful = {
    val newScroller = box.scroller.increment //todo: too hacky, please fix
    val newPos = if (box.scroller.increment.currentScroll == Rest) box.pos+box.scroller.scrollDirection else box.pos
    this.copy(box = box.copy(pos = newPos, scroller = newScroller))
  }

  override def mutate: Receive = {
    case Identity => this
    case Direction(dir) if box.scroller.currentScroll == Scroller.Stay =>
      this.copy(box.copy(playerSprite = box.playerSprite.animateSprite(OverworldSprite.Walk(dir)), scroller = box.scroller(dir)))
    case PauseMut => this.receive(SetChild(Some(makeMenu())))
    case ChangeJob(job) => this.copy(box.copy(playerSprite = OverworldSprite.FF1_PlayerSprite(job).copy(currentSprite = (box.playerSprite.currentSprite%27)+(27*job))))
    case _ => this
  }

  override def draw(grid: Grid): Unit = {
    grid.drawGrid(box.level.getSlice(Vector2(box.zoom*2+1), box.pos), box.level.tileSet, box.scroller.toVector2/box.scroller.scrollUnit)
    grid.drawOnCenter(box.playerSprite)
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
    new ListMenu(Statebox.ListMenuBox(0, items), subgrid)
  }



}
