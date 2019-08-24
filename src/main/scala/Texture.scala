import java.io.File

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL12._
import org.lwjgl.BufferUtils
import java.io.FileInputStream

import Util.PNGDecoder //todo: attempt to translate to scala

case class Texture(id: Int) extends Drawable
{
  override def drawRectangle(size: Vector2, position: Vector2, layer: Layer = Layer.CenterPlane): Unit = {
    glEnable(GL_TEXTURE_2D)
    Texture.bind(id)
    glColor3f(1f,1f,1f)
    glBegin(GL_POLYGON)
    glTexCoord2f(0, 0)
    glVertex3d(-1.0 + 2*position.x, -1.0 + 2*position.y, layer.position)
    glTexCoord2f(1, 0)
    glVertex3d(-1.0 + 2*(position.x+size.x), -1.0 + 2*position.y, layer.position)
    glTexCoord2f(1, 1)
    glVertex3d(-1.0 + 2*(position.x+size.x), -1.0 + 2*(position.y+size.y), layer.position)
    glTexCoord2f(0, 1)
    glVertex3d(-1.0 + 2*position.x, -1.0 + 2*(position.y+size.y), layer.position)
    glEnd()
    glDisable(GL_TEXTURE_2D)
  }
}

object Texture {

  private var textureCache: Map[String, Texture] = Map.empty

  def get(path: String): Texture = {
    if (textureCache.keys.toList.contains(path)) {
      textureCache(path)
    } else {
      val t = load(path)
      textureCache = textureCache.updated(path, t)
      t
    }
  }

  def load(path: String): Texture = {

    val img = new FileInputStream(new File(path))
    val dec = new PNGDecoder(img)
    val w = dec.getWidth
    val h = dec.getHeight
    val bpp = 4
    val buf = BufferUtils.createByteBuffer(bpp * w * h)

    dec.decode(buf, w * bpp, PNGDecoder.Format.RGBA)
    buf.flip()

    val id = glGenTextures()

    glEnable(GL_TEXTURE_2D)
    bind(id)

    //todo: figure out what all this shit actually does
    glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)

    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf)

    glDisable(GL_TEXTURE_2D)

    Texture(id)
  }

  def bind(id: Int): Unit = {
    glBindTexture(GL_TEXTURE_2D, id)
  }
}
