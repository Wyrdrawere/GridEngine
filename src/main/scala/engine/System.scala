package engine

trait System
{
  def update(world: World, deltaTime: Long): Unit
}


