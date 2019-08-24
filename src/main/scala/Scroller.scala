import Scroller.{Rest, Scroll, ScrollX, ScrollY, Stay}
import util.Vector2

case class Scroller
(
  scrollUnit: Int,
  scrollDirection: Vector2,
  currentScroll: Scroller.Scroll
) {

  def apply(dir: Vector2): Scroller = {
    val scroll = dir match {
      case Vector2.Up => ScrollY(-1)
      case Vector2.Down => ScrollY(1)
      case Vector2.Left => ScrollX(1)
      case Vector2.Right => ScrollX(-1)
    }
    this.copy(scrollDirection = dir, currentScroll = scroll)
  }

  def apply(newUnit: Int, dir: Vector2): Scroller = {
    this(dir).copy(scrollUnit = newUnit)
  }

  def increment: Scroller = {
    val nextScroll = currentScroll match {
      case ScrollX(x) => if (x.abs < scrollUnit) {ScrollX(x.sign * (x.abs+1))} else Rest
      case ScrollY(y) => if (y.abs < scrollUnit) {ScrollY(y.sign * (y.abs+1))} else Rest
      case Stay => Stay
      case Rest => Stay
    }
    this.copy(currentScroll = nextScroll)
  }
  val toVector2: Vector2 = currentScroll match {
    case ScrollX(x) => Vector2(-x,0)
    case ScrollY(y) => Vector2(0,-y)
    case Stay => Vector2(0)
    case Rest => Vector2(0)
  }

}

object Scroller {
  sealed trait Scroll
  case class ScrollX(progress: Int) extends Scroll
  case class ScrollY(progress: Int) extends Scroll
  case object Rest extends Scroll
  case object Stay extends Scroll

}

