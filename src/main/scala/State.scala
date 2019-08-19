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
        for (i <- input) i match {
          case KeyPressed(key) => if (inputDelay.isActive(key)) {
            inputDelay.control(i)
            receive(i)
          }

          case KeyHeld(key) => if (inputDelay.isActive(key)) {
            inputDelay.control(i)
            receive(i)
          }
          case KeyReleased(key) => if (inputDelay.isActive(key)) {
            inputDelay.control(i)
            receive(i)
          }
          case _ => receive(i)
        }
    }
  }

  final def render(): Unit = {
    draw(grid)
    for (child <- childState) child.render()
  }

  final private def receive(input: Input): Unit = (control orElse default)(input)
  final private def default: Controller = {case _ => ()}

  protected def idleUpdate(deltaTime: Long): Unit = {
    inputDelay.idleUpdate(deltaTime)
  }

  protected def update(deltaTime: Long): Unit

  protected def control: Controller

  protected def draw(grid: Grid): Unit
}