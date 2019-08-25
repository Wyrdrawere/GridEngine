package engine

import render.Layer
import util.Vector2

trait Drawable {
  def drawRectangle(size: Vector2, position: Vector2, layer: Layer = Layer.CenterPlane): Unit

  def drawRectanglePartial
  (
    size: Vector2,
    position: Vector2,
    layer: Layer = Layer.CenterPlane,
    lowerOffset: Vector2,
    upperOffset: Vector2
  ): Unit = drawRectangle(size, position, layer)
}
