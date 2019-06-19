import java.io.File

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL12._
import org.lwjgl.BufferUtils
import java.io.FileInputStream

import Tile.Texture
import Util.PNGDecoder

object TextureLoad {

  def apply(path: String): Texture = {

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

    glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)

    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf)

    Texture(id)
  }

  def bind(tex: Int): Unit = {
    glBindTexture(GL_TEXTURE_2D, tex)
  }
}
