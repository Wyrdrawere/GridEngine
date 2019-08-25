package systems

import components.{Position, Scroll}
import engine.Event.{Move, NewMove, NewRemoveComponent, RemoveComponent}
import engine.{Entity, NewEntity, NewSystem, NewWorld, State, World}
import util.{Config, Vector2}

object NewScroll extends NewSystem {

  override def update(newWorld: NewWorld, state: State, deltaTime: Long): Unit = {
    state.allEntities.view.foreach(e => e.modify[Scroll](s => {
      val prog = s.progress + 1
      if(prog >= s.max) {
        newWorld.emit(NewMove(e, s.direction))
        newWorld.emit(NewRemoveComponent(e, Scroll))
      }
      println(prog)
      Scroll(prog, s.max, s.direction)
    }))
  }

  def initMove(dir: Vector2, state: State)(newWorld: NewWorld): Unit = {
    state.selectEntities(Position).view.foreach(e => {
      if (!e.has(Scroll)) {
        e.attach(Scroll(0, Config.scrollUnit, dir))
      }
    })
  }

  def move(entity: NewEntity, direction: Vector2)(newWorld: NewWorld): Unit = {
    entity.modify[Position](p => Position(p.value+direction))
  }

}
