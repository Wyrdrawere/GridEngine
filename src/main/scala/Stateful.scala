import Input.{Back, DownArrow, Enter, LeftArrow, RightArrow, Space, UpArrow}
import Mutation.{CancelMut, ConfirmMut, Direction, DownMut, Identity, LeftMut, PauseMut, RightMut, SetChild, SetReturnMutation, UpMut}
import Stateful.Receive

trait Stateful {

  val box: Statebox
  val grid: Grid
  val childState: Option[Stateful]
  val returnMutation: Mutation

  def copy(box: Statebox = box, grid: Grid = grid, childState: Option[Stateful] = childState, returnMutation: Mutation = returnMutation): Stateful

  final def simulate(deltaTime: Long, input: Input): Stateful = childState match {
    case Some(child) => child.returnMutation match {
      case Identity => this.everyFrame(deltaTime).receive(SetChild(Some(child.simulate(deltaTime, input))))
      case _ =>
        val newState = this.receive(child.returnMutation)
        newState.childState match {
          case Some(newChild) => newState.receive(SetChild(Some(newChild.receive(SetReturnMutation(Identity)))))
          case None => newState
        }
    }
    case None => this.everyFrame(deltaTime).receive(inputToMutation(input))
  }

  final def render(): Unit = {
    draw(grid)
    childState match {
      case Some(child) => child.render()
      case None =>
    }
  }

  def receive(mutation: Mutation): Stateful = (default orElse mutate orElse catchCase)(mutation)
  def default: Receive = {
    case Identity => this
    case SetChild(state) => this.copy(childState = state)
    case SetReturnMutation(mutation) => this.copy(returnMutation = mutation)
  }
  def catchCase: Receive = {case _ => this}


  def everyFrame(deltaTime: Long): Stateful = this
  def inputToMutation(input: Input): Mutation = input match {
    case UpArrow => Direction(Vector2.Up)
    case DownArrow => Direction(Vector2.Down)
    case LeftArrow => Direction(Vector2.Left)
    case RightArrow => Direction(Vector2.Right)
    case Enter => ConfirmMut
    case Back => CancelMut
    case Space => PauseMut
    case _ => Identity
  }

  def mutate: Receive
  def draw(grid: Grid): Unit
}

object Stateful {
  type Receive = PartialFunction[Mutation, Stateful]
}
