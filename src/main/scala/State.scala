import Mutation.{Composite, CursorPosition, Identity, KeyHeld, KeyPressed, KeyReleased, MouseClicked, MouseHeld, MouseReleased, SetChildState, SetDelay, SetReturnMutation}

trait State {

  type Receive = PartialFunction[Mutation, Unit]

  private var grid: Grid = new Grid()
  private var childState: Option[State] = None
  private var returnMutation: Mutation = Identity
  private var inputDelay: InputDelay = InputDelay(Map.empty)
  private var sounds: List[Sound] = List.empty

  final def simulate(deltaTime: Long, input: Input): Unit = {
    this.update(deltaTime)
    childState match {
      case Some(child) => child.returnMutation match {
        case Identity => child.simulate(deltaTime, input)
        case m@_ =>
          this ! m
          for (child <- childState) child.simulate(deltaTime, input)
      }
      case None =>
        this.delayInput(deltaTime)
        this ! inputToMutation(input)
    }
  }

  final def render(): Unit = {
    sounds.foreach(s => s.play())
    sounds = List.empty
    draw(grid)
    for (child <- childState) child.render()
  }

  final protected def receive(mutation: Mutation): Unit = (default orElse mutate orElse catchCase)(mutation)
  final protected def !(mutation: Mutation): Unit = this.receive(mutation)
  final private def default: Receive = {
    case Identity => this
    case Composite(ms) => for (m <- ms) this ! m
    case SetChildState(child) => this.childState = child
    case SetReturnMutation(mutation) => this.returnMutation = mutation
  }
  final private def catchCase: Receive = {case _ => this}

  protected def inputToMutation(input: Input): Mutation = {
    val pos = CursorPosition(input.cursorPosition)

    val mClick = input.pressedButton.toList.map(b => MouseClicked(b, input.cursorPosition))
    val mHold = input.heldButton.toList.map(b => MouseHeld(b, input.cursorPosition))
    val mRelease = input.releasedButton.toList.map(b => MouseReleased(b, input.cursorPosition))

    val kPress = input.pressedKey.toList.filter(k => inputDelay.keyActive(k)).map(k => KeyPressed(k))
    val kHold = input.heldKey.toList.filter(k => inputDelay.keyActive(k)).map(k => KeyHeld(k))
    val kRelease = input.releasedKey.toList.map(k => KeyReleased(k))

    Composite(mClick ++ mHold ++ mRelease ++ kPress ++ kHold ++ kRelease :+ pos)
  }
  final private def delayInput(deltaTime: Long): Unit = {
    this.receive(SetDelay(inputDelay.cooldown(deltaTime)))
  }

  protected def update(deltaTime: Long): Unit = ()
  protected def mutate: Receive
  protected def draw(grid: Grid): Unit
}
