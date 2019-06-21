sealed trait Input

object Input {
  case object Space extends Input

  case object W extends Input
  case object S extends Input

  case object Up extends Input
  case object Down extends Input
  case object Left extends Input
  case object Right extends Input

  case object None extends Input

  def apply(key: Int): Input = key match {
    case 32 => Space

    case 83 => S
    case 87 => W

    case 262 => Right
    case 263 => Left
    case 264 => Down
    case 265 => Up
    case _ => ???
  }

  def printKey(key: Int):Unit = println(key)

}
