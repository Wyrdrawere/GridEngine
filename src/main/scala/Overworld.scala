import Overworld.Direction
import Overworld.Direction.{Down, Neutral, Up, Right, Left}
import Tile.Sprite

case class Overworld
(
  level: Array[Array[Int]],
  tileset: Map[Int, Sprite],
  position: (Int, Int),
  zoom: Int,
  transition: Int,
  inTransition: Option[Int],
  direction: Direction,
) extends State {

  override def simulate(): Overworld = {
    val newTransition = inTransition match {
      case Some(value) if value > 0 => Some(value-1)
      case Some(value) if value <= 0 => Some(transition)
    }
    this.copy(inTransition = newTransition)
  }

  override def render(): Unit = {
    val drawLevel = Level.getSlice(level, zoom, zoom, position)
    val offsetUnit = 1f/transition.toFloat
    val offset: (Float,Float) = direction match {
      case Up => (0,offsetUnit)
      case Down => (0,-offsetUnit)
      case Right => (offsetUnit,0)
      case Left => (-offsetUnit, 0)
      case Neutral => (0,0)
    }
    inTransition match {
      case Some(value) => Render.renderArrayFill(drawLevel, tileset, offset._1*value, offset._2*value)
      case None => Render.renderArrayFill(drawLevel, tileset)
    }
  }


}

object Overworld {
  trait Direction

  object Direction{
    case object Up extends Direction
    case object Down extends Direction
    case object Left extends Direction
    case object Right extends Direction
    case object Neutral extends Direction
  }
}
