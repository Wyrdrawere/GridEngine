package drawables

import engine.Drawable
import org.lwjgl.opengl.GL11._
import render.Layer
import util.Vector2

case class Color
(
  red: Float,
  green: Float,
  blue: Float,
  alpha: Float = 1
) extends Drawable
{
  def translucent(a: Float): Color = this.copy(alpha = a)

  override def drawRectangle(size: Vector2, position: Vector2, layer: Layer = Layer.CenterPlane): Unit = {
    glEnable(GL_BLEND)
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
    glColor4f(red, green, blue, alpha)
    glBegin(GL_POLYGON)
    glVertex3d(-1.0 + 2*position.x, -1.0 + 2*position.y, layer.position)
    glVertex3d(-1.0 + 2*(position.x+size.x), -1.0 + 2*position.y, layer.position)
    glVertex3d(-1.0 + 2*(position.x+size.x), -1.0 + 2*(position.y+size.y), layer.position)
    glVertex3d(-1.0 + 2*position.x, -1.0 + 2*(position.y+size.y), layer.position)
    glEnd()
    glDisable(GL_BLEND)
  }
}

object Color {
  val White = Color(1,1,1)
  val Black = Color(0,0,0)
  val Red = Color(1,0,0)
  val Green = Color(0,1,0)
  val Blue = Color(0,0,1)
  val Pink = Color(1,0,1)
  val Cyan = Color(0,1,1)
  val Yellow = Color(1,1,0)

  val simpleMap: Map[Int, Color] = Map(
    0 -> White,
    1 -> Black,
    2 -> Red,
    3 -> Green,
    4 -> Blue,
    5 -> Pink
  )
}
