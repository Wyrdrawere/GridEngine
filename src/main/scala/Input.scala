sealed trait Input

object Input {
  case object Space extends Input

  case object A extends Input
  case object B extends Input
  case object C extends Input
  case object D extends Input
  case object E extends Input
  case object F extends Input
  case object G extends Input
  case object H extends Input
  case object I extends Input
  case object J extends Input
  case object K extends Input
  case object L extends Input
  case object M extends Input
  case object N extends Input
  case object O extends Input
  case object P extends Input
  case object Q extends Input
  case object R extends Input
  case object S extends Input
  case object T extends Input
  case object U extends Input
  case object V extends Input
  case object W extends Input
  case object X extends Input
  case object Y extends Input
  case object Z extends Input

  case object Enter extends Input
  case object Back extends Input
  case object UpArrow extends Input
  case object DownArrow extends Input
  case object LeftArrow extends Input
  case object RightArrow extends Input

  case object None extends Input

  def apply(key: Int): Input = key match {
      //todo: completely map this through
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
    case 89 => Z
    case 90 => Y

    case 257 => Enter
    case 259 => Back
    case 262 => RightArrow
    case 263 => LeftArrow
    case 264 => DownArrow
    case 265 => UpArrow
    case _ =>
      println(key)
      Space
  }
}
