package systems

import util.Vector2
import engine.{System, World}

object SpriteAnimation extends System {

  sealed trait SpriteMode
  case class Walk(direction: Vector2) extends SpriteMode

  override def update(world: World, deltaTime: Long): Unit = ()
}
