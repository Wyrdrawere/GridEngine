sealed trait InputMouseButton

object InputMouseButton {
  case object Left extends InputMouseButton
  case object Right extends InputMouseButton
  case object Middle extends InputMouseButton
  case object NoMouseButton extends InputMouseButton

  def apply(button: Int): InputMouseButton = button match {
    case 0 => Left
    case 1 => Right
    case 2 => Middle
    case _ => NoMouseButton
  }
}
