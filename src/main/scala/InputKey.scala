sealed trait InputKey

object InputKey {
  case object Space extends InputKey

  case object A extends InputKey
  case object B extends InputKey
  case object C extends InputKey
  case object D extends InputKey
  case object E extends InputKey
  case object F extends InputKey
  case object G extends InputKey
  case object H extends InputKey
  case object I extends InputKey
  case object J extends InputKey
  case object K extends InputKey
  case object L extends InputKey
  case object M extends InputKey
  case object N extends InputKey
  case object O extends InputKey
  case object P extends InputKey
  case object Q extends InputKey
  case object R extends InputKey
  case object S extends InputKey
  case object T extends InputKey
  case object U extends InputKey
  case object V extends InputKey
  case object W extends InputKey
  case object X extends InputKey
  case object Y extends InputKey
  case object Z extends InputKey

  case object Enter extends InputKey
  case object Back extends InputKey
  case object UpArrow extends InputKey
  case object DownArrow extends InputKey
  case object LeftArrow extends InputKey
  case object RightArrow extends InputKey

  case object NoKey extends InputKey

  def apply(key: Int): InputKey = key match {
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
      NoKey
  }
}
