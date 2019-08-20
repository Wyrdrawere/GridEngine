sealed trait InputItem

object InputItem {
  case object Space extends InputItem

  case object A extends InputItem
  case object B extends InputItem
  case object C extends InputItem
  case object D extends InputItem
  case object E extends InputItem
  case object F extends InputItem
  case object G extends InputItem
  case object H extends InputItem
  case object I extends InputItem
  case object J extends InputItem
  case object K extends InputItem
  case object L extends InputItem
  case object M extends InputItem
  case object N extends InputItem
  case object O extends InputItem
  case object P extends InputItem
  case object Q extends InputItem
  case object R extends InputItem
  case object S extends InputItem
  case object T extends InputItem
  case object U extends InputItem
  case object V extends InputItem
  case object W extends InputItem
  case object X extends InputItem
  case object Y extends InputItem
  case object Z extends InputItem

  case object Enter extends InputItem
  case object Back extends InputItem
  case object UpArrow extends InputItem
  case object DownArrow extends InputItem
  case object LeftArrow extends InputItem
  case object RightArrow extends InputItem

  case object LeftMB extends InputItem
  case object RightMB extends InputItem
  case object MiddleMB extends InputItem

  case object NoItem extends InputItem

  def Key(key: Int): InputItem = key match {
      //todo: completely map this through. Or think it through first, LOTS of work otherwise
    case 32 => Space

    case 65 => A
    case 66 => B
    case 67 => C
    case 68 => D
    case 69 => E
    case 70 => F
    case 71 => G
    case 72 => H
    case 73 => I
    case 74 => J
    case 75 => K
    case 76 => L
    case 77 => M
    case 78 => N
    case 79 => O
    case 80 => P
    case 81 => Q
    case 82 => R
    case 83 => S
    case 84 => T
    case 85 => U
    case 86 => V
    case 87 => W
    case 88 => X
    case 89 => Z //qwertz/qwerty weirdness?
    case 90 => Y

    case 257 => Enter
    case 259 => Back
    case 262 => RightArrow
    case 263 => LeftArrow
    case 264 => DownArrow
    case 265 => UpArrow
    case _ =>
      println(key)
      NoItem
  }

  def Mouse(button: Int): InputItem = button match {
    case 0 => LeftMB
    case 1 => RightMB
    case 2 => MiddleMB
    case _ =>
      println(button)
      NoItem
  }
}
