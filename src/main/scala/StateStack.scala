import Stateful.{Next, Remove, Same}

case class StateStack
(
  states: List[Stateful]
) {

  def push(stateful: Stateful): StateStack = this.copy(states :+ stateful)
  def push(stateful: Option[Stateful]): StateStack = stateful match {
    case Some(s) => this.copy(states :+ s)
    case None => this
  }
  def pop(): StateStack = this.copy(states.reverse.drop(1).reverse)

  def simulate(deltaTime: Long, input: Input): StateStack = {
    val changedState = states.last.simulate(deltaTime, input)
    val updatedStack = pop().push(changedState)
    updatedStack.states.last.nextState match {
      case Next(s) => updatedStack.push(s)
      case Same => updatedStack
      case Remove => if(states.length > 1) updatedStack.pop() else updatedStack
    }
  }

  def render(grid: Grid): Unit = for (s <- states) s.render(grid)

}


