import InputKey.{Back, DownArrow, Enter, LeftArrow, RightArrow, S, Space, UpArrow, W}
import Mutation.{CancelMut, Composite, ConfirmMut, CursorPosition, Direction, Identity, InputMut, InputMutation, KeyHeld, KeyPressed, KeyReleased, MouseClicked, MouseHeld, MouseReleased, PauseMut, SetBox, SetChild, SetReturnMutation}

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
    case None => this.everyFrame(deltaTime).receive(InputMutation(input))
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
    case Composite(ms) => ms match {
      case Nil => this
      case x :: xs => this.receive(x).receive(Composite(xs))
    }
  }
  final private def catchCase: Receive = {case _ => this}

  protected def everyFrame(deltaTime: Long): Stateful = this
  protected def inputToMutation(input: Input): Mutation = {
    val pos = CursorPosition(input.cursorPosition)

    val mClick = input.pressedButton.toList.map(b => MouseClicked(b))
    val mHold = input.pressedButton.toList.map(b => MouseHeld(b))
    val mRelease = input.pressedButton.toList.map(b => MouseReleased(b))

    val kPress = input.pressedKey.toList.map(k => KeyPressed(k))
    val kHold = input.heldKey.toList.map(k => KeyHeld(k))
    val kRelease = input.releasedKey.toList.map(k => KeyReleased(k))

    var keyMutations: List[Mutation] = kPress ++ kHold ++ kRelease
    keyMutations = keyMutations.filter(m => !Config.keyMap.toList.map(x => x._1).contains(m))

    val c = Composite(mClick ++ mHold ++ mRelease ++ keyMutations.map(m => Config.keyMap(m)))
    println(c)
    c
  }

  protected def mutate: Receive
  protected def draw(grid: Grid): Unit
}
