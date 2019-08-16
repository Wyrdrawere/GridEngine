import InputKey.{Back, DownArrow, Enter, LeftArrow, RightArrow, S, Space, UpArrow, W}
import Mutation.{CancelMut, ConfirmMut, Direction, Identity, InputMut, PauseMut, SetBox, SetChild, SetReturnMutation}

trait Stateful {

  type Receive = PartialFunction[Mutation, Stateful]

  val box: Statebox
  val grid: Grid
  val childState: Option[Stateful]
  val returnMutation: Mutation

  protected def copy
  (
    box: Statebox = box,
    grid: Grid = grid,
    childState: Option[Stateful] = childState,
    returnMutation: Mutation = returnMutation
  ): Stateful //todo: find way to implement this here

  final def simulate(deltaTime: Long, input: InputKey): Stateful = childState match {
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

  final protected def receive(mutation: Mutation): Stateful = (default orElse mutate orElse catchCase)(mutation)
  final private def default: Receive = {
    case Identity => this
    case SetBox(b) => this.copy(box = b)
    case SetChild(state) => this.copy(childState = state)
    case SetReturnMutation(mutation) => this.copy(returnMutation = mutation)
  }
  final private def catchCase: Receive = {case _ => this}

  protected def everyFrame(deltaTime: Long): Stateful = this
  protected def inputToMutation(input: InputKey): Mutation = input match {
    case UpArrow => Direction(Vector2.Up)
    case DownArrow => Direction(Vector2.Down)
    case LeftArrow => Direction(Vector2.Left)
    case RightArrow => Direction(Vector2.Right)
    case W => InputMut(W)
    case S => InputMut(S)
    case Enter => ConfirmMut
    case Back => CancelMut
    case Space => PauseMut
    case _ => Identity
  }

  protected def mutate: Receive
  protected def draw(grid: Grid): Unit
}
