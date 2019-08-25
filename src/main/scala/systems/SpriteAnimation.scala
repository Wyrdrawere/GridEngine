package systems

import components.{AnimatedSprite, Background, Player, Scroll}
import engine.{System, World, State}
import util.Vector2

object SpriteAnimation extends System  {

  sealed trait SpriteMode
  case class Walk(direction: Vector2) extends SpriteMode

  override def update(newWorld: World, state: State, deltaTime: Long): Unit = ()

  def animatePlayer(mode: SpriteMode, state: State)(world: World): Unit = {
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
