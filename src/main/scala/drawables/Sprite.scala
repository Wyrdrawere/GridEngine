package drawables

import engine.Drawable
import org.lwjgl.opengl.GL11._
import render.Layer
import util.Vector2

case class Sprite //todo: new name because font
(
  id: Int,
  minX: Float,
  maxX: Float,
  minY: Float,
  maxY: Float,
) extends Drawable {

  override def drawRectangle(size: Vector2, position: Vector2, layer: Layer = Layer.CenterPlane): Unit = {
    glEnable(GL_TEXTURE_2D)
    glEnable(GL_BLEND)
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
    Texture.bind(id)
    glColor3f(1f, 1f, 1f)
    glBegin(GL_POLYGON)
    glTexCoord2f(minX, maxY)
    glVertex3d(-1.0 + 2 * position.x, -1.0 + 2 * position.y, layer.position)
    glTexCoord2f(maxX, maxY)
    glVertex3d(-1.0 + 2 * (position.x + size.x), -1.0 + 2 * position.y, layer.position)
    glTexCoord2f(maxX, minY)
    glVertex3d(-1.0 + 2 * (position.x + size.x), -1.0 + 2 * (position.y + size.y), layer.position)
    glTexCoord2f(minX, minY)
    glVertex3d(-1.0 + 2 * position.x, -1.0 + 2 * (position.y + size.y), layer.position)
    glEnd()
    glDisable(GL_BLEND)
    glDisable(GL_TEXTURE_2D)
  }

  override def drawRectanglePartial
  (
    size: Vector2,
    position: Vector2,
    layer: Layer = Layer.CenterPlane,
    lowerOffset: Vector2,
    upperOffset: Vector2
  ): Unit = {
    val newMinX = minX+(maxX-minX)*lowerOffset.x
    val newMinY = minY+(maxY-minY)*upperOffset.y
    val newMaxX = maxX-(maxX-minX)*upperOffset.x
    val newMaxY = maxY-(maxY-minY)*lowerOffset.y
    Sprite(id, newMinX, newMaxX, newMinY, newMaxY).drawRectangle(size, position, layer)
  }
}

object Sprite {

  private var spriteSheetCache: Map[String, Map[Int, Sprite]] = Map.empty

  def get(path: String, size: Vector2, tileSize: Vector2): Map[Int, Sprite] = {
    if (spriteSheetCache.keys.toList.contains(path)) {
      spriteSheetCache(path)
    } else {
      val s = Sprite.TextureToTileSet(Texture.get(path), size, tileSize)
      spriteSheetCache = spriteSheetCache.updated(path, s)
      s
    }
  }

  private def TextureToTileSet(texture: Texture, size: Vector2, tileSize: Vector2): Map[Int, Sprite] = {
    val xAmount = (size.x / tileSize.x).toInt
    val yAmount = (size.y / tileSize.y).toInt
    var spriteMap: Map[Int, Sprite] = Map.empty
    for (x <- 0 until xAmount; y <- 0 until yAmount) {
      spriteMap = spriteMap.updated(
        y * xAmount + x,
        Sprite(
          texture.id,
          x.toFloat / xAmount,
          (x + 1).toFloat / xAmount,
          y.toFloat / yAmount,
          (y + 1).toFloat / yAmount,
        ))
    }
    spriteMap
  }

  lazy val ff1_Spritesheet: Map[Int, Sprite] = Sprite.get("src/resources/Sprite/ff1-classes.png", Vector2(972,432), Vector2(36))
  lazy val basicBackground: Map[Int, Sprite] = Sprite.TextureToTileSet(Texture.get("src/resources/Tileset/basictiles.png"),Vector2(128,240), Vector2(16))
}