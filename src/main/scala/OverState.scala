import InputItem.{DownArrow, LeftArrow, RightArrow, UpArrow}
import Scroller.{Rest, Stay}
import util.{Config, Vector2}

class OverState extends State {

  var level: Level = Level.TestDungeon
  var playerSprite: OverworldSprite = OverworldSprite.FF1_PlayerSprite(0)
  var pos: Vector2 = Vector2(0)
  var scroller: Scroller = new Scroller(Config.scrollUnit, Vector2(0), Scroller.Stay)
  var stepSound: Sound = Sound.load("src/resources/Sound/step.ogg")

  override protected def update(deltaTime: Long): Unit = {
    val newScroller = scroller.increment //todo: too hacky, please fix
    val newPos = if (scroller.increment.currentScroll == Rest) pos + scroller.scrollDirection else pos
    pos = newPos
    scroller = newScroller

    if(Input.isActive(LeftArrow)) {
      Input.delay(LeftArrow, 0)
      move(Vector2.Left)
    }
    if(Input.isActive(RightArrow)) {
      Input.delay(RightArrow, 0)
      move(Vector2.Right)
    }
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
