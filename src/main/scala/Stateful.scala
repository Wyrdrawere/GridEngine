trait Stateful {
  def simulate(deltaTime: Long, input: Input): Stateful
  def render(): Unit
}