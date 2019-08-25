package systems

import components.{AnimatedSprite, Player}
import engine.{NewSystem, NewWorld, State}
import util.Vector2

object NewDrawAnim extends NewSystem {
  override def update(newWorld: NewWorld, state: State, deltaTime: Long): Unit = {
    state.selectEntities(AnimatedSprite).view.foreach(e => {
      if(e.has(Player)) {
        for (s <- e.get(AnimatedSprite)) state.grid.drawOnCenter(s.spriteSheet(s.currentSprite), Vector2(1))
      } else {

      }
    })
  }
}
