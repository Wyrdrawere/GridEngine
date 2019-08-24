import org.lwjgl.opengl.GL11.{GL_BLEND, GL_ONE_MINUS_SRC_ALPHA, GL_POLYGON, GL_SRC_ALPHA, GL_TEXTURE_2D, glBegin, glBlendFunc, glColor3f, glDisable, glEnable, glEnd, glTexCoord2f, glVertex3d}
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

  override def drawRectanglePartial(size: Vector2, position: Vector2, offset: Vector2, edge: (Boolean, Boolean, Boolean)): Unit = {
    var newMinX = minX
    var newMaxX = maxX
    var newMinY = minY
    var newMaxY = maxY
    val xUnit = maxX - minX
    val yUnit = maxY - minY
    if (edge._1) offset.x match {
      case x if x > 0 => if(edge._3) newMaxX = minX + xUnit * x else newMinX = minX + xUnit * x
      case x if x < 0 => if(edge._3) newMinX = maxX + xUnit * x else newMaxX = maxX + xUnit * x
      case 0 =>
    }
    if (edge._2) offset.y match {
      case y if y < 0 => if(edge._3) newMaxY = minY - yUnit * y else newMinY = minY - yUnit * y
      case y if y > 0 => if(edge._3) newMinY = maxY - yUnit * y else newMaxY = maxY - yUnit * y
      case 0 =>
    }
    Sprite(id, newMinX, newMaxX, newMinY, newMaxY).drawRectangle(size, position)
  }

  override def drawRectanglePartialProto
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

  def TextureToTileSet(texture: Texture, size: Vector2, tileSize: Vector2): Map[Int, Sprite] = {
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

  val ff1_Spritesheet: Map[Int, Sprite] = Sprite.get("src/resources/Sprite/ff1-classes.png", Vector2(972,432), Vector2(36))
  val basicBackground: Map[Int, Sprite] = Sprite.TextureToTileSet(Texture.get("src/resources/Tileset/basictiles.png"),Vector2(128,240), Vector2(16))
}