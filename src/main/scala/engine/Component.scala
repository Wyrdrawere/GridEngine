package engine

trait Component
{
  def update(
    deltaTime: Long,
    world: World
  ): Unit = ()
}
