import org.lwjgl.opengl.GL11.{GL_POLYGON, glBegin, glColor4f, glEnd, glVertex3d}

case class Color(red: Float, green: Float, blue: Float, alpha: Float) extends Drawable {

  def translucent(a: Float): Color = this.copy(alpha = a)
}

object Color {
  val White = Color(1,1,1,1)
  val Black = Color(0,0,0,1)
  val Red = Color(1,0,0,1)
  val Green = Color(0,1,0,1)
  val Blue = Color(0,0,1,1)
  val Pink = Color(1,0,1,1)

  val simpleMap: Map[Int, Color] = Map(
    0 -> White,
    1 -> Black,
    2 -> Red,
    3 -> Green,
    4 -> Blue,
    5 -> Pink
  )
}
