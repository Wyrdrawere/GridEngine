import InputKey.{Back, DownArrow, LeftArrow, RightArrow, S, Space, UpArrow, W}
import Mutation._
import Scroller.Rest

class Overworld
(
  override val box: Statebox.OverworldBox,
  override val grid: Grid,
  override val childState: Option[Stateful] = None,
  override val returnMutation: Mutation = Identity,
  override val inputDelay: InputDelay = InputDelay(Map.empty)
) extends Stateful {

  var c: Vector2 = Vector2(0)

  override def copy
  (box: Statebox = box,
   grid: Grid = grid,
   childState: Option[Stateful] = childState,
   returnMutation: Mutation = returnMutation,
   inputDelay: InputDelay = inputDelay): Stateful = new Overworld(
    box.asInstanceOf[Statebox.OverworldBox], grid, childState, returnMutation, inputDelay
  )

  override def everyFrame(deltaTime: Long): Stateful = {
    val newScroller = box.scroller.increment //todo: too hacky, please fix
    val newPos = if (box.scroller.increment.currentScroll == Rest) box.pos + box.scroller.scrollDirection else box.pos
    receive(SetBox(box.copy(pos = newPos, scroller = newScroller)))
  }

  override def mutate: Receive = {
    case Identity => this

      //todo: remove mouse stuff from here
    case MouseClicked(button, pos) =>
      println(grid.getGridCoordinate(pos))
      this


    case CursorPosition(pos) =>
      c = pos
      this


    case KeyPressed(UpArrow) if box.scroller.currentScroll == Scroller.Stay => move(Vector2.Up)
    case KeyPressed(DownArrow) if box.scroller.currentScroll == Scroller.Stay => move(Vector2.Down)
    case KeyPressed(LeftArrow) if box.scroller.currentScroll == Scroller.Stay => move(Vector2.Left)
    case KeyPressed(RightArrow) if box.scroller.currentScroll == Scroller.Stay => move(Vector2.Right)
    case KeyHeld(UpArrow) if box.scroller.currentScroll == Scroller.Stay => move(Vector2.Up)
    case KeyHeld(DownArrow) if box.scroller.currentScroll == Scroller.Stay => move(Vector2.Down)
    case KeyHeld(LeftArrow) if box.scroller.currentScroll == Scroller.Stay => move(Vector2.Left)
    case KeyHeld(RightArrow) if box.scroller.currentScroll == Scroller.Stay => move(Vector2.Right)

    case KeyPressed(W) => receive(SetBox(box.copy(zoom = if (box.zoom > 1) box.zoom-1 else box.zoom)))
    case KeyPressed(S) => receive(SetBox(box.copy(zoom = box.zoom+1)))
    case KeyHeld(W) => receive(SetBox(box.copy(zoom = if (box.zoom > 1) box.zoom-1 else box.zoom)))
    case KeyHeld(S) => receive(SetBox(box.copy(zoom = box.zoom+1)))

    case KeyPressed(Space) => receive(SetChild(Some(makeMenu())))
    case KeyPressed(Back) => receive(SetBox(box.copy(pos = Vector2(0))))

    case ChangeJob(job) => receive(SetBox(box.copy(playerSprite = OverworldSprite.FF1_PlayerSprite(job).copy(currentSprite = (box.playerSprite.currentSprite % 27) + (27 * job)))))

  }

  override def draw(grid: Grid): Unit = {
    grid.drawGrid(box.level.getSlice(Vector2(box.zoom * 2 + 1), box.pos), box.level.tileSet, box.scroller.toVector2 / box.scroller.scrollUnit)
    grid.drawOnCenter(box.playerSprite)
    grid.setDimensions(grid.getDimensions*4)
    grid.getGridCoordinate(c) match {
      case Some(v) => grid.drawOnGrid(Color.Green, Vector2(v.xi+1f, v.yi), Vector2(v.xi-v.x, v.yi-v.y), Vector2(1f))
      case None =>
    }
    grid.setDimensions(grid.getDimensions/4)
  }

  private def move(dir: Vector2) = {
    receive(SetBox(box.copy(playerSprite = box.playerSprite.animateSprite(OverworldSprite.Walk(dir)), scroller = box.scroller(dir))))
  }

  private def makeMenu(): ListMenu = {
    val items = Map(
      0 -> (Text("Up", Text.DarkGrayFont), SetReturnMutation(KeyPressed(UpArrow))),
      1 -> (Text("Down", Text.DarkGrayFont), SetReturnMutation(KeyPressed(DownArrow))),
      2 -> (Text("Left", Text.DarkGrayFont), SetReturnMutation(KeyPressed(LeftArrow))),
      3 -> (Text("Right", Text.DarkGrayFont), SetReturnMutation(KeyPressed(RightArrow))),
      4 -> (Text("Jobs", Text.DarkGrayFont), MakeSubMenu),
    )
    val subgrid = new Grid(Vector2(0.25f, 0.75f), relativePosition = Vector2(0.75f, 0.25f))
    new ListMenu(Statebox.ListMenuBox(0, items), subgrid)
  }

}
