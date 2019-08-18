import Input.{Controller, KeyHeld, KeyPressed, KeyReleased}
import InputKey.UpArrow

trait State {

  protected var inputDelay: NewInputDelay = new NewInputDelay(0)
  protected var grid: Grid = new Grid()
  protected var childState: Option[State] = None

  final def simulate(deltaTime: Long, input: List[Input]): Unit = {
    idleUpdate(deltaTime)
    childState match {
      case Some(child) => child.simulate(deltaTime, input)
      case None =>
        update(deltaTime)
        receive(input.head)
        for (i <- input) {
          println(i)
          control(i)
        }
        for (i <- input) i match {
          case KeyPressed(key) if inputDelay.isActive(key) =>
            println("pressed " + key)
            this.receive(i)
          case KeyHeld(key) if inputDelay.isActive(key) => receive(i)
          case KeyReleased(key) if inputDelay.isActive(key) => receive(i)
          case _ => receive(i) //todo: input still wonky
        }
    }
  }

  final def render(): Unit = {
    draw(grid)
    for (child <- childState) child.render()
  }

  final private def receive(input: Input): Unit = control(input) orElse default(input)
  final private def default(input: Input): Controller = {case _ => println("default")}

  protected def idleUpdate(deltaTime: Long): Unit = ()

  protected def update(deltaTime: Long): Unit

  protected def control(input: Input): Controller

  protected def draw(grid: Grid): Unit
}