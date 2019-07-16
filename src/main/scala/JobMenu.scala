import Mutation.{CancelMut, ChangeJob, ConfirmMut, Direction, Identity, SetChild, SetReturnMutation}
import Stateful.Receive

class JobMenu
(
  override val box: Statebox.JobMenuBox,
  override val grid: Grid,
  override val childState: Option[Stateful] = None,
  override val returnMutation: Mutation = Identity
) extends Stateful {


  override def copy
  (box: Statebox = box,
   grid: Grid = grid,
   childState: Option[Stateful] = childState,
   returnMutation: Mutation = returnMutation): Stateful = new JobMenu(
    box.asInstanceOf[Statebox.JobMenuBox], grid, childState, returnMutation
  )

  override def mutate: Receive = {
    case Direction(Vector2.Up) => this.copy(box.copy(cursor = if (box.cursor.y < 1) box.cursor+Vector2.Up else box.cursor))
    case Direction(Vector2.Down) => this.copy(box.copy(cursor = if (box.cursor.y > 0) box.cursor+Vector2.Down else box.cursor))
    case Direction(Vector2.Left) => this.copy(box.copy(cursor = if (box.cursor.x > 0) box.cursor+Vector2.Left else box.cursor))
    case Direction(Vector2.Right) => this.copy(box.copy(cursor = if (box.cursor.x < box.items.size/2-1) box.cursor+Vector2.Right else box.cursor))
    case ConfirmMut => this.receive(SetReturnMutation(box.items(box.cursor)._2))
    case CancelMut => this.receive(SetReturnMutation(SetChild(None)))
    case _ => this
  }

  override def draw(grid: Grid): Unit = {

    println(returnMutation)

    grid.setDimensions(Vector2(3))
    for(x <- 0 to 3; y <- 0 to 3) {
      grid.drawOnGrid(Color.Blue.translucent(0.75f), Vector2(x,y), Vector2(0))
    }
    grid.setDimensionsSquare(7)
    val cursorpos = Vector2(box.cursor.xi-(box.cursor.xi % 2), box.cursor.yi % 2)
    grid.drawOnGrid(Color.Pink.translucent(0.5f), itemsToCoordinate(cursorpos), Vector2(0))
    for(x <- box.cursor.xi to box.cursor.xi+1; y <- 0 to 1) {
      box.items.get(Vector2(x,y)) match {
        case Some(d) => grid.drawOnGrid(d._1, itemsToCoordinate(Vector2(x-box.cursor.xi,y)), Vector2(0))
        case None =>
      }
    }
  }

  def itemsToCoordinate(coord: Vector2): Vector2 = {
    val posX = if (coord.xi % 2 == 0) 3 else 5
    val posY = if (coord.yi % 2 == 0) 3 else 5
    Vector2(posX, posY)
  }


}

object JobMenu {

  def SpriteSheetConvert(sprites: Map[Int, Sprite], cols: Int): Map[Vector2, (Sprite, Mutation)] = {
    var s = sprites.toList.filter(x => x._1 % cols == 0).sortBy(x => x._1)

    var xc = 0
    var yc = 1

    var ret: Map[Vector2, (Sprite, Mutation)] = Map.empty

    for (x <- s.indices) {
      ret = ret.updated(Vector2(xc, yc), (s(x)._2, ChangeJob(x)))
      if (x % 2 == 1) xc = xc+1
      yc = (yc-1).abs % 2
    }
    ret
  }

}
