package systems

import components.{Position, Scroll}
import engine.Event.{Move, RemoveComponent}
import engine.{NewWorld, State, World, newSystem}
import util.{Config, Vector2}

object newScroll extends newSystem {

  override def update(newWorld: NewWorld, state: State, deltaTime: Long): Unit = {
    state.allEntities.foreach(e => e.modify[Scroll](s => {
      val prog = s.progress + 1
      if(prog >= s.max) {
        newWorld.emit(Move(e, s.direction))
        newWorld.emit(RemoveComponent(e, Scroll))
      }
      Scroll(prog, s.max, s.direction)
    }))
  }

  def initMove(dir: Vector2)(newWorld: NewWorld, state: State): Unit = {
    newWorld.selectEntities(Position).view.foreach(e => {
      if (!e.has(Scroll)) {
        e.attach(Scroll(0, Config.scrollUnit, dir))
      }
    })
  }

}
