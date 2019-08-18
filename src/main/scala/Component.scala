import Input.Controller

trait Component {

  def idleUpdate(deltaTime: Long): Unit
  def update(deltaTime: Long): Unit
  def receive(input: Input): Unit = control(input) orElse default(input)
  def default(input: Input): Controller = {case _ => ()}
  def control(input: Input): Controller

}
