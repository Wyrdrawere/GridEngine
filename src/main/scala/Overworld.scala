import Input.{S, W}
import Tile.Sprite

case class Overworld
(
  level: Level,
  tileSet: Map[Int,Sprite],
  pos: (Int,Int),
  zoom: Int,
) extends Stateful {

  override def simulate(deltaTime: Long, input: Input): Overworld = input match {
    case W if zoom >= 1 => this.copy(zoom = zoom-1)
    case S => this.copy(zoom = zoom +1)
    case _ => this
  }

  override def render(): Unit = {
    val slice = level.getSlice(zoom, zoom, pos)
    Render.renderArrayFill(slice, tileSet)
  }
}

object Overworld {


}