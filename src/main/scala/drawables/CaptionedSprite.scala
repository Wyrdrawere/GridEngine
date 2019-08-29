package drawables

import engine.Drawable
import render.Layer
import util.Vector2

case class CaptionedSprite(sprite: Sprite, text: Text, spriteRatio: Float) extends Drawable {
  override def drawRectangle(size: Vector2, position: Vector2, layer: Layer): Unit = {
    if(spriteRatio <= 0 || spriteRatio > 1) throw new Exception("Ratio must be inside (0,1] but is " + spriteRatio)
    text.drawRectangle(size, position)
    sprite.drawRectangle(size*spriteRatio, position+Vector2.Up*(size.y*spriteRatio))
  }
}
