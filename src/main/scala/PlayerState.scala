import Input._

case class PlayerState(xPos: Int, yPos: Int, direction: Boolean, zoom: Int) {

  def control(input: Input): PlayerState = input match {
    case W =>  if (zoom > 1) this.copy(zoom = zoom-1) else this
    case S => this.copy(zoom = zoom+1)

    case Up => this.copy(yPos = yPos+1)
    case Down => this.copy(yPos = yPos-1)
    case Left => this.copy(xPos = xPos-1, direction = false)
    case Right => this.copy(xPos = xPos+1, direction = true)
    case _ => ???
  }
}

object PlayerState{

}