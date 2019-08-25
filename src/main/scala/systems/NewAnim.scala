package systems

import components.{AnimatedSprite, Background, Player, Scroll}
import engine.{NewSystem, NewWorld, State}
import systems.SpriteAnimation.SpriteMode

object NewAnim extends NewSystem  {
  override def update(newWorld: NewWorld, state: State, deltaTime: Long): Unit = ()

  def animatePlayer(mode: SpriteMode, state: State)(world: NewWorld): Unit = {
    if(!state.selectEntities(Background).view.map(_.has(Scroll)).head) {
      state.selectEntities(Player).view.foreach(_.modify[AnimatedSprite](sprite => {
        val nextMode = sprite.modeMap.get(mode) match {
          case Some(v) => v
          case None => List(0)
        }
        var nextSprite = nextMode.head
        if (nextMode.contains(sprite.currentSprite)) {
          val nextId = nextMode.indexOf(sprite.currentSprite) + 1
          if (nextMode.indices.contains(nextId)) {
            nextSprite = nextMode(nextId)
          }
        }
        AnimatedSprite(sprite.spriteSheet, sprite.modeMap, nextSprite)
      }))
    }
  }
}
