package systems

import components.{Background, Player, Position}
import engine.{Entity, Event, State, System, World}
import util.Vector2

object ImmediateMovement extends System {

  override def update(newWorld: World, state: State, deltaTime: Long): Unit = ()

  def Move(entity: Entity, direction: Vector2)(world: World, state: State): Unit = {
    entity.modify[Position](p => Position(p.value + direction))
  }

  def Move(entity: Entity, direction: Vector2, minPos: Vector2, maxPos: Vector2)(world: World, state: State): Unit = {
    entity.modify[Position](p => Position((p.value + direction).clamp(minPos, maxPos)))
  }
}
