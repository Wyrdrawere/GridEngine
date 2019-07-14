import Input.{Back, DownArrow, Enter, LeftArrow, RightArrow, Space, UpArrow}
import Mutation.{CancelMut, ConfirmMut, Direction, DownMut, Identity, LeftMut, PauseMut, RightMut, SetChild, SetReturnMutation, UpMut}

trait Stateful {

  val grid: Grid
  val childState: Option[Stateful]
  val returnMutation: Mutation

  final def simulate(deltaTime: Long, input: Input): Stateful = childState match {
    case Some(child) => child.returnMutation match {
      case Identity => this.everyFrame(deltaTime).mutate(SetChild(Some(child.simulate(deltaTime, input))))
      case _ =>
        val newState = this.mutate(child.returnMutation)
        newState.childState match {
          case Some(newChild) => newState.mutate(SetChild(Some(newChild.mutate(SetReturnMutation(Identity)))))
          case None => newState
        }
    }
    case None => this.everyFrame(deltaTime).mutate(inputToMutation(input))
  }

  final def render(): Unit = {
    draw(grid)
    childState match {
      case Some(child) => child.render()
      case None =>
    }
  }

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

  def mutate(mutation: Mutation): Stateful
  def draw(grid: Grid): Unit
}

object Stateful {
  object BaseStateful extends Stateful {
    override val grid: Grid = new Grid()
    override val childState: Option[Stateful] = None
    override val returnMutation: Mutation = Identity

    override def mutate(mutation: Mutation): Stateful = this

    override def draw(grid: Grid): Unit = ()
  }
}
