import Input.{Controller, KeyHeld, KeyPressed}
import InputKey.UpArrow
import Scroller.{Rest, Stay}

class OverState extends State {

  var level: Level = Level.TestDungeon
  var playerSprite: OverworldSprite = OverworldSprite.FF1_PlayerSprite(0)
  var pos: Vector2 = Vector2(0)
  var scroller: Scroller = new Scroller(Config.scrollUnit, Vector2(0), Scroller.Stay)
  inputDelay = new NewInputDelay(10000)

  override protected def update(deltaTime: Long): Unit = {
    val newScroller = scroller.increment //todo: too hacky, please fix
    val newPos = if (scroller.increment.currentScroll == Rest) pos + scroller.scrollDirection else pos
    pos = newPos
    scroller = newScroller
  }

  override protected def control: Controller = {
    case KeyPressed(UpArrow)|KeyHeld(UpArrow) if scroller.currentScroll == Scroller.Stay => move(Vector2.Up)
  }

  override protected def draw(grid: Grid): Unit = {
    grid.drawGrid(level.getSlice(Vector2(11), pos), level.tileSet, scroller.toVector2 / scroller.scrollUnit)
    grid.drawOnCenter(playerSprite)
  }

  private def move(dir: Vector2): Unit = {
    playerSprite = playerSprite.animateSprite(OverworldSprite.Walk(dir))
    scroller = scroller(dir)
  }
}
