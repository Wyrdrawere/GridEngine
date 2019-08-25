package components

import engine.{Component, ComponentKey, Drawable}
import systems.SpriteAnimation.SpriteMode

case class AnimatedSprite(spriteSheet: Map[Int, Drawable], modeMap: Map[SpriteMode, List[Int]], currentSprite: Int) extends Component

case object AnimatedSprite extends ComponentKey[AnimatedSprite]
