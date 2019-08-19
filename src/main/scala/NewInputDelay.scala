import Input.{Controller, KeyHeld, KeyPressed}
class NewInputDelay(delay: Long) extends Component {

  def isActive(key: InputKey): Boolean = !keys.contains(key)

  private var keys: Map[InputKey, Long] = Map.empty

  private def add(key: InputKey): Unit = {
    keys = keys.updated(key, delay)
  }

  override def idleUpdate(deltaTime: Long): Unit = {
    keys = keys.map{case (k,c) => (k, c-deltaTime)}
    keys = keys.filter{case (k,c) => c > 0}
  }

  override def update(deltaTime: Long): Unit = ()

  override def control: Controller = {
    case KeyPressed(key) => add(key)
    case KeyHeld(key) => add(key)
    case _ =>
  }
}
