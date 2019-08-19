import Input.Controller

trait Component {

  def idleUpdate(deltaTime: Long): Unit
  def update(deltaTime: Long): Unit
  def receive(input: Input): Unit = (control orElse default)(input)
  def default: Controller = {case _ => ()}
  def control: Controller

}
