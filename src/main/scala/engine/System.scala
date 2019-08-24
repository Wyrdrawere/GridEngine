package engine

trait System
{
  def update(
    world: World,
    delta: Double,
  ): Unit
}


