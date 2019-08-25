package engine

import deprecate.World

trait Component
{
  def update(
    deltaTime: Long,
    world: World
  ): Unit = ()
}
