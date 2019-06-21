import Input.{Down, S, Up, W, Left, Right}
import Overworld.Scroll
import Overworld.Scroll.{None, ScrollDown, ScrollLeft, ScrollRight, ScrollUp}
import Tile.Sprite

case class Overworld
(
  level: Level,
  tileSet: Map[Int,Sprite],
  pos: (Int,Int),
  zoom: Int,
  scroll: Scroll,
  inputReady: Boolean
) extends Stateful {

  override def simulate(deltaTime: Long, input: Input): Overworld = {

    var newState: Overworld = everyFrame(deltaTime)

    if(inputReady) {
      input match {
        case W if zoom >= 1 => newState.copy(zoom = zoom-1)
        case S => newState.copy(zoom = zoom +1)
        case Up => newState.copy(scroll = ScrollUp(0), inputReady = false)
        case Down => newState.copy(scroll = ScrollDown(0), inputReady = false)
        case Left => newState.copy(scroll = ScrollLeft(0), inputReady = false)
        case Right => newState.copy(scroll = ScrollRight(0), inputReady = false)
        case _ => newState
      }
    } else newState
  }

  override def render(): Unit = {
    val slice = level.getSlice(zoom*2+1, zoom*2+1, pos)
    scroll match {
      case ScrollUp(s) => Render.renderArrayFill(slice, tileSet, 0f, -s.toFloat*(1f/Config.scrollUnit))
      case ScrollDown(s) => Render.renderArrayFill(slice, tileSet, 0f, s.toFloat*(1f/Config.scrollUnit))
      case ScrollLeft(s) => Render.renderArrayFill(slice, tileSet, s.toFloat*(1f/Config.scrollUnit), 0f)
      case ScrollRight(s) => Render.renderArrayFill(slice, tileSet, -s.toFloat*(1f/Config.scrollUnit), 0f)
      case None =>   Render.renderArrayFill(slice, tileSet)
    }
  }

  private def everyFrame(deltaTime: Long): Overworld = {

    //fixme: abstract this!
    scroll match {
      case ScrollUp(s) =>
        if(s<Config.scrollUnit) {
          this.copy(scroll = ScrollUp(s+1), inputReady = s==Config.scrollUnit-1)
        }
        else {
          this.copy(pos = (pos._1, pos._2+1), scroll = None)
        }
      case ScrollDown(s) =>
        if(s<Config.scrollUnit) {
          this.copy(scroll = ScrollDown(s+1), inputReady = s==Config.scrollUnit-1)
        }
        else {
          this.copy(pos = (pos._1, pos._2-1), scroll = None)
        }
      case ScrollLeft(s) =>
        if(s<Config.scrollUnit) {
          this.copy(scroll = ScrollLeft(s+1), inputReady = s==Config.scrollUnit-1)
        }
        else {
          this.copy(pos = (pos._1-1, pos._2), scroll = None)
        }
      case ScrollRight(s) =>
        if(s<Config.scrollUnit) {
          this.copy(scroll = ScrollRight(s+1), inputReady = s==Config.scrollUnit-1)
        }
        else {
          this.copy(pos = (pos._1+1, pos._2), scroll = None)
        }
      case None => this
    }
  }

  private def validInput(input: Input): Boolean = true

}

object Overworld {

  def testWorld = Overworld(
    Level.RandomLevel(50,50,120),
    Tile.Sprite.TextureToTileSet(TextureLoad("src/resources/Tileset/basictiles.png"),128,240,8,15),
    (0,0),
    5,
    None,
    true)

  sealed trait Scroll
  object Scroll {
    case class ScrollUp(progress: Int) extends Scroll
    case class ScrollDown(progress: Int) extends Scroll
    case class ScrollLeft(progress: Int) extends Scroll
    case class ScrollRight(progress: Int) extends Scroll
    case object None extends Scroll
  }
}