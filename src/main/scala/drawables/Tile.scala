package drawables

import engine.Drawable
import render.Layer
import util.Vector2

case class Tile
(
  content: List[Sprite],
) extends Drawable {
  override def drawRectangle(size: Vector2, position: Vector2, layer: Layer = Layer.CenterPlane): Unit = {
    for (s <- content) {
      s.drawRectangle(size, position, layer)
    }
  }

  override def drawRectanglePartial(size: Vector2, position: Vector2, layer: Layer, lowerOffset: Vector2, upperOffset: Vector2): Unit = {
    for (s <- content) {
      s.drawRectanglePartial(size, position, layer, lowerOffset, upperOffset)
    }
  }


}
