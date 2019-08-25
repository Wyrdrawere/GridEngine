package systems

import components.{AnimatedSprite, Player}
import engine.{System, World}
import util.Vector2

object DrawSprite extends System {
  override def update(world: World, deltaTime: Long): Unit = {
    world.selectEntities(AnimatedSprite).view.foreach(e => {
      if(e.has(Player)) {
        for (s <- e.get(AnimatedSprite)) world.mainGrid.drawOnCenter(s.spriteSheet(s.currentSprite), Vector2(1))
      } else {

      }
    })
  }
}
