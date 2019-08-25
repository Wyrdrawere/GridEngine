package engine

trait NewSystem {

  def update(newWorld: NewWorld, state: State, deltaTime: Long): Unit

}
