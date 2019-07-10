import Input.{DownArrow, LeftArrow, RightArrow, S, Space, UpArrow, W}
import Scroll.{ScrollX, ScrollY, Stay}
import Stateful.{Next, NextState, Same}

case class Overworld
(
  level: Level,
  tileSet: Map[Int,Drawable],
  playerSprite: PlayerSprite,
  pos: (Int,Int),
  zoom: Int,
  scroll: Scroll,
  inputReady: Boolean, //todo: stateful def?
  next: NextState = Same
) extends Stateful {

  override val nextState: NextState = next

  override def simulate(deltaTime: Long, input: Input): Overworld = {

    val newState: Overworld = everyFrame(deltaTime)

    if(inputReady) {
      val nextSprite: PlayerSprite = playerSprite.simulate(deltaTime, input)
      input match {
        case W if zoom >= 2 => newState.copy(zoom = zoom-1)
        case S => newState.copy(zoom = zoom +1)
        case UpArrow => newState.copy(playerSprite = nextSprite, scroll = ScrollY(-1), inputReady = false)
        case DownArrow => newState.copy(playerSprite = nextSprite, scroll = ScrollY(1), inputReady = false)
        case LeftArrow => newState.copy(playerSprite = nextSprite, scroll = ScrollX(1), inputReady = false)
        case RightArrow => newState.copy(playerSprite = nextSprite, scroll = ScrollX(-1), inputReady = false)
        case Space => this.copy(next = Next(ListMenu.testMenu))
        case _ => newState
      }
    } else newState
  }

  override def render(grid: Grid): Unit = {
    val slice = level.getSlice(zoom*2+1, zoom*2+1, pos)
    scroll match {
      case ScrollX(x) => grid.drawGrid(slice, tileSet, Vector2((1-x.toFloat)/Config.scrollUnit, 0f))
      case ScrollY(y) => grid.drawGrid(slice, tileSet, Vector2(0f, (1-y.toFloat)/Config.scrollUnit))
      case Stay => grid.drawGrid(slice, tileSet, Vector2(0))
    }
    playerSprite.render(grid)
  }

  private def everyFrame(deltaTime: Long): Overworld = {

    //todo: better, but still not enough. Might not belong here. (Maybe like ugly casedefinitions in grid: companion object)
    scroll match {
      case ScrollX(x) =>
        val nextScroll = scroll.increment
        val ready = nextScroll == Stay
        this.copy(
          pos = if(ready) (pos._1-x.sign, pos._2) else pos,
          scroll = nextScroll,
          inputReady = ready,
          next = Same)
      case ScrollY(y) =>
        val nextScroll = scroll.increment
        val ready = nextScroll == Stay
        this.copy(
          pos = if(ready) (pos._1, pos._2-y.sign) else pos,
          scroll = nextScroll,
          inputReady = ready,
          next = Same)
      case Stay => this.copy(next = Same)
    }
  }
}

object Overworld {


  def testWorld(level: Level) = Overworld(
    level,
    Sprite.get("src/resources/Tileset/basictiles.png",Vector2(128,240),Vector2(16,16)),
    PlayerSprite.FF1_PlayerSprite(8),
    (0,0),
    5,
    Stay,
    inputReady = true)
}