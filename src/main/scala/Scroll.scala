import Scroll.{ScrollX, ScrollY, Stay}

sealed trait Scroll {
  def increment: Scroll = this match {
    case ScrollX(x) => if (x.abs < Config.scrollUnit) {ScrollX(x.sign * (x.abs+1))} else Stay
    case ScrollY(y) => if (y.abs < Config.scrollUnit) {ScrollY(y.sign * (y.abs+1))} else Stay
    case Stay => Stay
  }

}

object Scroll {
  case class ScrollX(progress: Int) extends Scroll
  case class ScrollY(progress: Int) extends Scroll
  case object Stay extends Scroll
}