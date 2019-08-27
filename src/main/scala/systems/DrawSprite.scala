package systems

import components.{AnimatedSprite, Player}
import engine.{System, World, State}
import util.Vector2

object DrawSprite extends System {
  override def update(newWorld: World, state: State, deltaTime: Long): Unit = {
    state.selectEntities(AnimatedSprite).view.foreach(e => {
      if(e.has(Player)) {
        for (s <- e.get(AnimatedSprite)) state.grid.drawOnCenter(s.spriteSheet(s.currentSprite), Vector2(1))
      } else {

      }
    })
  }
}
