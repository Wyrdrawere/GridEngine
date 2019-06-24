import Input.{Down, Left, Right, S, Up, W}
import Scroll.{ScrollX, ScrollY, Stay}
import Tile.Sprite

case class Overworld
(
  level: Level,
  tileSet: Map[Int,Sprite],
  playerSprite: PlayerSprite,
  pos: (Int,Int),
  zoom: Int,
  scroll: Scroll,
  inputReady: Boolean //todo: stateful def?
) extends Stateful {

  override def simulate(deltaTime: Long, input: Input): Overworld = {

    var newState: Overworld = everyFrame(deltaTime)

    if(inputReady) {
      val nextSprite: PlayerSprite = playerSprite.simulate(deltaTime, input)
      input match {
        case W if zoom >= 2 => newState.copy(zoom = zoom-1)
        case S => newState.copy(zoom = zoom +1)
        case Up => newState.copy(playerSprite = nextSprite, scroll = ScrollY(-1), inputReady = false)
        case Down => newState.copy(playerSprite = nextSprite, scroll = ScrollY(1), inputReady = false)
        case Left => newState.copy(playerSprite = nextSprite, scroll = ScrollX(1), inputReady = false)
        case Right => newState.copy(playerSprite = nextSprite, scroll = ScrollX(-1), inputReady = false)
        case _ => newState
      }
    } else newState
  }

  override def render(): Unit = {
    val slice = level.getSlice(zoom*2+1, zoom*2+1, pos)
    scroll match {
      case ScrollX(x) => Render.renderArrayFill(slice, tileSet, x.toFloat/Config.scrollUnit, 0f)
      case ScrollY(y) => Render.renderArrayFill(slice, tileSet, 0f, y.toFloat/Config.scrollUnit)
      case Stay => Render.renderArrayFill(slice, tileSet)
    }
    playerSprite.render()
  }

  private def everyFrame(deltaTime: Long): Overworld = {

    //todo: better, but still not enough
    scroll match {
      case ScrollX(x) => {
        val nextScroll = scroll.increment
        val ready = nextScroll == Stay
        this.copy(
          pos = if(ready) (pos._1-x.sign, pos._2) else pos,
          scroll = nextScroll,
          inputReady = ready)
      }
      case ScrollY(y) => {
        val nextScroll = scroll.increment
        val ready = nextScroll == Stay
        this.copy(
          pos = if(ready) (pos._1, pos._2-y.sign) else pos,
          scroll = nextScroll,
          inputReady = ready)
      }
      case Stay => this
    }
  }
}

object Overworld {

  def testWorld(level: Level) = Overworld(
    level,
    Tile.Sprite.TextureToTileSet(TextureLoad("src/resources/Tileset/basictiles.png"),128,240,8,15),
    PlayerSprite.FF1_PlayerSprite(6),
    (0,0),
    5,
    Stay,
    true)

}