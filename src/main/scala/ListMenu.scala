import Mutation.{CancelMut, ChangeJob, ConfirmMut, Direction, DownMut, Identity, MakeSubMenu, SetBox, SetChild, SetReturnMutation, UpMut}
import Stateful.Receive

class ListMenu
(
  override val box: Statebox.ListMenuBox,

  override val grid: Grid,
  override val childState: Option[Stateful] = None,
  override val returnMutation: Mutation = Identity

) extends Stateful {

  override def copy
  (box: Statebox = box,
   grid: Grid = grid,
   childState: Option[Stateful] = childState,
   returnMutation: Mutation = returnMutation): Stateful = new ListMenu(
    box.asInstanceOf[Statebox.ListMenuBox], grid, childState, returnMutation
  )

  override def mutate: Receive = {
    case Direction(Vector2.Up) => receive(SetBox(box.copy(cursor = if (box.cursor > 0) box.cursor - 1 else box.cursor)))
    case Direction(Vector2.Down) => receive(SetBox(box.copy(cursor = if (box.cursor < box.items.size - 1) box.cursor + 1 else box.cursor)))
    case ConfirmMut => receive(box.items(box.cursor)._2)
    case CancelMut => receive(SetReturnMutation(SetChild(None)))
    case MakeSubMenu => receive(SetChild(Some(makeJobMenu)))
    case c@ChangeJob(_) => receive(SetReturnMutation(c))
  }

  override def draw(grid: Grid): Unit = {
    grid.setDimensions(Vector2(3))
    for(x <- 0 to 3; y <- 0 to 3) {
      grid.drawOnGrid(Color.Blue.translucent(0.75f), Vector2(x,y), Vector2(0))
    }
    grid.setDimensionsSquare(box.items.toList.map(x => x._2._1).collect{case t: Text => t}.map(t => t.string.length).max+4)
    val dim = grid.getDimensions
    for(x <- box.items.toList.indices) {
      val size = box.items(x)._1 match {
        case Text(s, f) => Vector2(s.length)
        case _ => Vector2(1)
      }
      grid.drawOnGrid(box.items(x)._1, Vector2(3, dim.y-2-x), Vector2(0), size)
    }
    grid.drawOnGrid(Color.Pink, Vector2(2, dim.y-2-box.cursor), Vector2(0))
  }

  private def makeJobMenu: JobMenu = {
    val items = JobMenu.SpriteSheetConvert(Sprite.ff1_Spritesheet, 27)
    val subgrid = new Grid(Vector2(0.75f), Vector2(0,0.25f))
    new JobMenu(Statebox.JobMenuBox(Vector2(0,1), items), subgrid)
  }

}


