import Mutation.{IdentMut, RemoveReturnMut}

trait State {

  val grid: Grid
  val parentState: State
  val childState: Option[State]
  val returnMutation: Option[Mutation]

  final def simulate(deltaTime: Long, input: Input): State = {
    childState match {
      case Some(c) => c.returnMutation match {
        case Some(m) => mutate(m).updateChild(c.mutate(RemoveReturnMut))
        case None => updateChild(c.simulate(deltaTime, input))
      }
      case None => everyFrame(deltaTime).mutate(inputToMutation(input))
    }
  }

  final def render(): Unit = {
    draw()
    childState match {
      case Some(c) => c.render()
      case None =>
    }
  }

  def everyFrame(deltaTime: Long): State = this
  def mutate(mutation: Mutation): State
  def inputToMutation(input: Input): Mutation
  def draw(): Unit

  def updateChild(child: State): State
}

object State {
  object BaseState extends State {
    override val grid: Grid = new Grid()
    override val parentState: State = this
    override val childState: Option[State] = None
    override val returnMutation: Option[Mutation] = None

    override def mutate(mutation: Mutation): State = this

    override def inputToMutation(input: Input): Mutation = IdentMut

    override def draw(): Unit = ()

    override def updateChild(child: State): State = this //todo: once State is fully defined, rework this to be some sort of copy
  }
}


