import java.io.File

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL12._
import org.lwjgl.BufferUtils
import java.io.FileInputStream

import Util.PNGDecoder

object Texture {

  def apply(path: String): Int = {

    val img = new FileInputStream(new File(path))
    val dec = new PNGDecoder(img)
    val w = dec.getWidth
    val h = dec.getHeight
    val bpp = 4
    val buf = BufferUtils.createByteBuffer(bpp * w * h)

    dec.decode(buf, w * bpp, PNGDecoder.Format.RGBA)
    buf.flip()

    val id = glGenTextures()
    val target = GL_TEXTURE_2D

    glEnable(target)
    glBindTexture(target, id)

    glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)

    glTexImage2D(target, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf)

    id
  }

  def OpenPathSet(): Map[Int, String] = Map(
    0 -> "/home/taiki/Pictures/textures/OpenSquare/Open Square Path Grass.png",
    1 -> "/home/taiki/Pictures/textures/OpenSquare/Open Square Path Cross.png",
    2 -> "/home/taiki/Pictures/textures/OpenSquare/Open Square Path Horizontal.png",
    3 -> "/home/taiki/Pictures/textures/OpenSquare/Open Square Path Vertical.png",
    4 -> "/home/taiki/Pictures/textures/OpenSquare/Open Square Path Corner NW.png",
    5 -> "/home/taiki/Pictures/textures/OpenSquare/Open Square Path Corner NE.png",
    6 -> "/home/taiki/Pictures/textures/OpenSquare/Open Square Path Corner SW.png",
    7 -> "/home/taiki/Pictures/textures/OpenSquare/Open Square Path Corner SE.png",
    )
}
