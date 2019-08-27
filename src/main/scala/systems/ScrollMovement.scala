package systems

import components.{Position, Scroll}
import engine.Event.{Move, RemoveComponent}
import engine.{Entity, System, World, State}
import util.{Config, Vector2}

object ScrollMovement extends System {

  override def update(World: World, state: State, deltaTime: Long): Unit = {
    state.allEntities.view.foreach(e => e.modify[Scroll](s => {
      val prog = s.progress + 1
      if(prog >= s.max) {
        World.emit(Move(e, s.direction), state)
        World.emit(RemoveComponent(e, Scroll), state)
      }
      Scroll(prog, s.max, s.direction)
    }))
  }

  def initMove(dir: Vector2)(World: World, state: State): Unit = {
    state.selectEntities(Position).view.foreach(e => {
      if (!e.has(Scroll)) {
        e.attach(Scroll(0, Config.scrollUnit, dir))
      }
    })
  }

  def move(entity: Entity, direction: Vector2)(World: World, state: State): Unit = {
    entity.modify[Position](p => Position(p.value+direction))
  }

}
