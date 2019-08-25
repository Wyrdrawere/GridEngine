package systems

import components.{Position, Scroll}
import engine.Event.{Move, RemoveComponent}
import engine.{Entity, System, World, State}
import util.{Config, Vector2}

object ScrollMovement extends System {

  override def update(newWorld: World, state: State, deltaTime: Long): Unit = {
    state.allEntities.view.foreach(e => e.modify[Scroll](s => {
      val prog = s.progress + 1
      if(prog >= s.max) {
        newWorld.emit(Move(e, s.direction))
        newWorld.emit(RemoveComponent(e, Scroll))
      }
      Scroll(prog, s.max, s.direction)
    }))
  }

  def initMove(dir: Vector2, state: State)(newWorld: World): Unit = {
    state.selectEntities(Position).view.foreach(e => {
      if (!e.has(Scroll)) {
        e.attach(Scroll(0, Config.scrollUnit, dir))
      }
    })
  }

  def move(entity: Entity, direction: Vector2)(newWorld: World): Unit = {
    entity.modify[Position](p => Position(p.value+direction))
  }

}
