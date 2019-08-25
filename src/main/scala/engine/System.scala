package engine

trait System {

  def update(newWorld: World, state: State, deltaTime: Long): Unit

}
