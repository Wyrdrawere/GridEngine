package engine

trait System {

  def update(World: World, state: State, deltaTime: Long): Unit

  //todo: many Systems happen to additionally use functions like
  // foo => (world, state) => unit
  // research into abstracting that here

  //todo: potentially split this into updatesystem|rendersystem
  // as rendersystems dont use the provided world/deltatime
}
