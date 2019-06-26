import org.lwjgl.opengl.GL11.{GL_BLEND, GL_ONE_MINUS_SRC_ALPHA, GL_POLYGON, GL_SRC_ALPHA, glBegin, glBlendFunc, glColor3f, glDisable, glEnable, glEnd, glTexCoord2f, glVertex3d}

case class Sprite
(
  id: Int,
  minX: Float,
  maxX: Float,
  minY: Float,
  maxY: Float
) extends Drawable
{
  override def drawRectangle(size: Vector2, position: Vector2): Unit = {
    glEnable(GL_BLEND)
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
    Texture.bind(id)
    glColor3f(1f,1f,1f)
    glBegin(GL_POLYGON)
    glTexCoord2f(maxX, maxY)
    glVertex3d(-1.0 + 2*position.x, -1.0 + 2*position.y, 0)
    glTexCoord2f(minX, maxY)
    glVertex3d(-1.0 + 2*(position.x+size.x), -1.0 + 2*position.y, 0)
    glTexCoord2f(minX, minY)
    glVertex3d(-1.0 + 2*(position.x+size.x), -1.0 + 2*(position.y+size.y), 0)
    glTexCoord2f(maxX, minY)
    glVertex3d(-1.0 + 2*position.x, -1.0 + 2*(position.y+size.y), 0)
    glEnd()
    glDisable(GL_BLEND)
  }
}

object Sprite {
  def TextureToTileSet(texture: Texture, width: Int, height: Int, tileWidth: Int, tileHeight: Int): Map[Int, Drawable] = {
    var xAmount = width/tileWidth
    var yAmount = height/tileHeight
    var spriteMap: Map[Int, Sprite] = Map.empty
    for(x <- 0 until xAmount; y <- 0 until yAmount) {
      spriteMap = spriteMap.updated(
        y*xAmount+x,
        Sprite(
          texture.id,
          x.toFloat/xAmount,
          (x+1).toFloat/xAmount,
          y.toFloat/yAmount,
          (y+1).toFloat/yAmount,
        ))
    }
    spriteMap
  }
}