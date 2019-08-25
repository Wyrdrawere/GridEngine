package deprecate

import components.{AnimatedSprite, Background, Player, Scroll}
import util.Vector2

object SpriteAnimation extends System {

  sealed trait SpriteMode
  case class Walk(direction: Vector2) extends SpriteMode

  override def update(world: World, deltaTime: Long): Unit = ()

  def animatePlayer(mode: SpriteMode)(deltaTime: Long, world: World): Unit = {
    if(!world.selectEntities(Background).view.map(_.has(Scroll)).head) {
      world.selectEntities(Player).view.foreach(_.modify[AnimatedSprite](sprite => {
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
