package engine

sealed trait GlobalEvent

object GlobalEvent {

  case class Push(state: State) extends GlobalEvent

  case object Pop extends GlobalEvent

}


