import Input._

case class PlayerState(xPos: Int, yPos: Int, lastDirection: Input, zoom: Int) {

  def control(input: Input): PlayerState = input match {
    case W =>  if (zoom > 1) this.copy(zoom = zoom-1) else this
    case S => this.copy(zoom = zoom+1)

    case Up => this.copy(yPos = yPos+1, lastDirection = Up)
    case Down => this.copy(yPos = yPos-1, lastDirection = Down)
    case Left => this.copy(xPos = xPos-1, lastDirection = Left)
    case Right => this.copy(xPos = xPos+1, lastDirection = Right)
    case _ => ???
  }
}

object PlayerState{

}