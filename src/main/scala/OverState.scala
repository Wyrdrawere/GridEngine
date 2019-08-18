import Input.{Controller, KeyHeld, KeyPressed}
import InputKey.{DownArrow, LeftArrow, RightArrow, UpArrow}
import Scroller.{Rest, Stay}

class OverState extends State {

  var level: Level = Level.TestDungeon
  var playerSprite: OverworldSprite = OverworldSprite.FF1_PlayerSprite(0)
  var pos: Vector2 = Vector2(0)
  var scroller: Scroller = new Scroller(Config.scrollUnit, Vector2(0), Scroller.Stay)
  var stepSound: Sound = Sound.load("src/resources/Sound/step.ogg")
  inputDelay = new NewInputDelay(300)

  override protected def update(deltaTime: Long): Unit = {
    val newScroller = scroller.increment //todo: too hacky, please fix
    val newPos = if (scroller.increment.currentScroll == Rest) pos + scroller.scrollDirection else pos
    pos = newPos
    scroller = newScroller
  }

  override protected def control: Controller = {
    case KeyPressed(UpArrow) if scroller.currentScroll == Scroller.Stay => move(Vector2.Up)
    case KeyPressed(DownArrow) if scroller.currentScroll == Scroller.Stay => move(Vector2.Down)
    case KeyPressed(LeftArrow) if scroller.currentScroll == Scroller.Stay => move(Vector2.Left)
    case KeyPressed(RightArrow) if scroller.currentScroll == Scroller.Stay => move(Vector2.Right)
    case KeyHeld(UpArrow) if scroller.currentScroll == Scroller.Stay => move(Vector2.Up)
    case KeyHeld(DownArrow) if scroller.currentScroll == Scroller.Stay => move(Vector2.Down)
    case KeyHeld(LeftArrow) if scroller.currentScroll == Scroller.Stay => move(Vector2.Left)
    case KeyHeld(RightArrow) if scroller.currentScroll == Scroller.Stay => move(Vector2.Right)
  }

      override protected def draw(grid: Grid): Unit = {
    grid.drawGrid(level.getSlice(Vector2(11), pos), level.tileSet, scroller.toVector2 / scroller.scrollUnit)
    grid.drawOnCenter(playerSprite)
  }

  private def move(dir: Vector2): Unit = {
    playerSprite = playerSprite.animateSprite(OverworldSprite.Walk(dir))
    scroller = scroller(dir)
    stepSound.play()
  }
}
