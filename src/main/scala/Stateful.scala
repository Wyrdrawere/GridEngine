import Stateful.{NextState, Same}

trait Stateful { //todo: think about state abstract class wich holds a list of statefuls. Also make stateful/mutation a thing again

  val nextState: NextState = Same

  def simulate(deltaTime: Long, input: Input): Stateful
  def render(grid: Grid): Unit //todo: make private and public def, have stateful set gridDimensions itself. Alternative: associate stateful with grid
}

object Stateful {

  sealed trait NextState
  case class Next(stateful: Stateful) extends NextState
  case object Same extends NextState
  case object Remove extends NextState

}