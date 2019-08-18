import Mutation._

trait Stateful {

  type Receive = PartialFunction[Mutation, Stateful]

  val box: Statebox
  val grid: Grid
  val childState: Option[Stateful]
  val returnMutation: Mutation
  val inputDelay: InputDelay

  protected def copy
  (
    box: Statebox = box,
    grid: Grid = grid,
    childState: Option[Stateful] = childState,
    returnMutation: Mutation = returnMutation,
    inputDelay: InputDelay = InputDelay(Map.empty)
  ): Stateful //todo: find way to implement this here

  final def simulate(deltaTime: Long, input: InputData): Stateful = childState match {
    case Some(child) => child.returnMutation match {
      case Identity => this.everyFrame(deltaTime).receive(SetChild(Some(child.simulate(deltaTime, input))))
      case _ =>
        val newState = this.receive(child.returnMutation)
        newState.childState match {
          case Some(newChild) => newState.receive(SetChild(Some(newChild.receive(SetReturnMutation(Identity)))))
          case None => newState
        }
    }
    case None => this.everyFrame(deltaTime).delayInput(deltaTime).receive(inputToMutation(input))
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
    case SetDelay(delay) => this.copy(inputDelay = delay)
    case Composite(ms) => ms match {
      case Nil => this
      case x :: xs => this.receive(x).receive(Composite(xs))
    }
  }
  final private def catchCase: Receive = {case _ => this}

  final private def delayInput(deltaTime: Long): Stateful = {
    this.receive(SetDelay(inputDelay.cooldown(deltaTime)))
  }

  protected def everyFrame(deltaTime: Long): Stateful = this
  protected def inputToMutation(input: InputData): Mutation = {
    val pos = CursorPosition(input.cursorPosition)

    val mClick = input.pressedButton.toList.map(b => MouseClicked(b, input.cursorPosition))
    val mHold = input.heldButton.toList.map(b => MouseHeld(b, input.cursorPosition))
    val mRelease = input.releasedButton.toList.map(b => MouseReleased(b, input.cursorPosition))

    val kPress = input.pressedKey.toList.map(k => KeyPressed(k))
    val kHold = input.heldKey.toList.map(k => KeyHeld(k))
    val kRelease = input.releasedKey.toList.map(k => KeyReleased(k))

    Composite(mClick ++ mHold ++ mRelease ++ kPress ++ kHold ++ kRelease :+ pos)
  }

  protected def mutate: Receive
  protected def draw(grid: Grid): Unit
}
