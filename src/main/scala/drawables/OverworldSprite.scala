package drawables

import drawables.OverworldSprite.SpriteMode
import render.{Drawable, Layer}
import util.Vector2

case class OverworldSprite
(
  spriteSheet: Map[Int, Drawable],
  modeMap: Map[SpriteMode, List[Int]],
  currentSprite: Int,
) extends Drawable {

  def animateSprite(mode: SpriteMode): OverworldSprite = {
    val nextMode = modeMap(mode)
    var nextSprite = nextMode.head
    if (nextMode.contains(currentSprite)) {
      val nextId = nextMode.indexOf(currentSprite) + 1
      if (nextMode.indices.contains(nextId)) {
        nextSprite = nextMode(nextId)
      }
    }
    this.copy(currentSprite = nextSprite)
  }

  override def drawRectangle(size: Vector2, position: Vector2, layer: Layer = Layer.CenterPlane): Unit = {
    spriteSheet(currentSprite).drawRectangle(size, position, layer)
  }

  override def drawRectanglePartial(size: Vector2, position: Vector2, offset: Vector2, edge: (Boolean, Boolean, Boolean)): Unit = {
    spriteSheet(currentSprite).drawRectanglePartial(size, position, offset, edge)
  }

  override def drawRectanglePartialProto(size: Vector2, position: Vector2, layer: Layer, lowerOffset: Vector2, upperOffset: Vector2): Unit = {
    spriteSheet(currentSprite).drawRectanglePartialProto(size, position, layer, lowerOffset, upperOffset)
  }

}

object OverworldSprite {

  def FF1_PlayerSprite(job: Int): OverworldSprite = OverworldSprite(
    Sprite.get("src/resources/output.Sprite/ff1-classes.png", Vector2(972, 432), Vector2(36, 36)),
    Map(
      Walk(Vector2.Up) -> List(27*job+2, 27*job+3),
      Walk(Vector2.Down) -> List(27*job, 27*job+1),
      Walk(Vector2.Right) -> List(27*job+4, 27*job+5),
      Walk(Vector2.Left) -> List(27*job+6, 27*job+7)
    ),
    27*job
  )

  sealed trait SpriteMode
  case class Walk(dir: Vector2) extends SpriteMode
}
