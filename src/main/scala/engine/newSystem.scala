package engine

trait newSystem {

  def update(newWorld: NewWorld, state: State, deltaTime: Long): Unit

}
