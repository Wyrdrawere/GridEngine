package systems

import components.Scroll
import engine.Event.{Move, RemoveComponent}
import engine.{System, World}

object ScrollMovement extends System
{
  override def update(world: World, deltaTime: Long): Unit = {
    world.selectEntities(Scroll).view.foreach(e => e.modify[Scroll](s => {
      val prog = s.progress + 1
      if(prog >= s.max) {
        world.emit(Move(e, s.direction))
        world.emit(RemoveComponent(e, Scroll))
      }
      println(s)
      Scroll(prog, s.max, s.direction)
    }))
  }
}
