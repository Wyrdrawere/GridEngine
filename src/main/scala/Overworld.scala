import Overworld.Direction
import Overworld.Direction.Neutral
import Tile.Sprite

class Overworld(val level: Array[Array[Int]], val tileset: Map[Int, Sprite]) extends State {

  val position: (Int, Int) = (0,0)
  val zoom: Int = 5

  val transition: Int = 8
  val inTransition: Option[Int] = None
  val direction: Direction = Neutral

  override def simulate(): State = this

  override def render(): Unit = {
    val drawLevel = Level.getSlice(level, zoom, zoom, position)
    inTransition match {
      case Some(value) => Render.renderArrayFill(drawLevel, tileset, )
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
