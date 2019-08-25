package deprecate

import components.{Position, Scroll}
import engine.Event.{Move, RemoveComponent}
import util.{Config, Vector2}

object ScrollMovement extends System
{
  override def update(world: World, deltaTime: Long): Unit = {
    world.selectEntities(Scroll).view.foreach(e => e.modify[Scroll](s => {
      val prog = s.progress + 1
      if(prog >= s.max) {
        world.emit(Move(e, s.direction))
        world.emit(RemoveComponent(e, Scroll))
      }
      Scroll(prog, s.max, s.direction)
    }))
  }

  def initMove(dir: Vector2)(deltaTime: Long, world: World): Unit = {
      world.selectEntities(Position).view.foreach(e => {
        if (!e.has(Scroll)) {
          e.attach(Scroll(0, Config.scrollUnit, dir))
        }
      })
  }

  def move(entity: Entity, direction: Vector2)(deltaTime: Long, world: World): Unit = {
      entity.modify[Position](p => Position(p.value+direction))
  }
}
