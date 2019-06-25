import Config.{windowWidth, windowHeight}
import org.lwjgl.opengl.GL11._

object Render {

  //todo: put this somewhere else, cant be global
  var gridXSize: Float = 0
  var gridYSize: Float = 0

  def Rect(xPos: Float, yPos: Float, xSize: Float, ySize: Float, color: Color): Unit = {
    glColor4f(color.red, color.green, color.blue, color.alpha)
    glBegin(GL_POLYGON)
    glVertex3d(-1.0 + 2*xPos/windowWidth, -1.0 + 2*yPos/windowHeight, 0)
    glVertex3d(-1.0 + 2*xPos/windowWidth + 2*xSize/windowWidth, -1.0 + 2*yPos/windowHeight, 0)
    glVertex3d(-1.0 + 2*xPos/windowWidth + 2*xSize/windowWidth, -1.0 + 2*yPos/windowHeight + 2*ySize/windowHeight, 0)
    glVertex3d(-1.0 + 2*xPos/windowWidth, -1.0 + 2*yPos/windowHeight + 2*ySize/windowHeight, 0)
    glEnd()
  }

  def Rect(xPos: Float, yPos: Float, xSize: Float, ySize: Float, tex: Texture): Unit = {
    Texture.bind(tex.id)
    glColor3f(1f,1f,1f)
    glBegin(GL_POLYGON)
    glTexCoord2f(0, 0)
    glVertex3d(-1.0 + 2*xPos/windowWidth, -1.0 + 2*yPos/windowHeight, 0)
    glTexCoord2f(1, 0)
    glVertex3d(-1.0 + 2*xPos/windowWidth + 2*xSize/windowWidth, -1.0 + 2*yPos/windowHeight, 0)
    glTexCoord2f(1, 1)
    glVertex3d(-1.0 + 2*xPos/windowWidth + 2*xSize/windowWidth, -1.0 + 2*yPos/windowHeight + 2*ySize/windowHeight, 0)
    glTexCoord2f(0, 1)
    glVertex3d(-1.0 + 2*xPos/windowWidth, -1.0 + 2*yPos/windowHeight + 2*ySize/windowHeight, 0)
    glEnd()
  }

  def Rect(xPos: Float, yPos: Float, xSize: Float, ySize: Float, sprite: Sprite): Unit = {
    glEnable(GL_BLEND)
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
    Texture.bind(sprite.id)
    glColor3f(1f,1f,1f)
    glBegin(GL_POLYGON)
    glTexCoord2f(sprite.maxX, sprite.maxY)
    glVertex3d(-1.0 + 2*xPos/windowWidth, -1.0 + 2*yPos/windowHeight, 0)
    glTexCoord2f(sprite.minX, sprite.maxY)
    glVertex3d(-1.0 + 2*xPos/windowWidth + 2*xSize/windowWidth, -1.0 + 2*yPos/windowHeight, 0)
    glTexCoord2f(sprite.minX, sprite.minY)
    glVertex3d(-1.0 + 2*xPos/windowWidth + 2*xSize/windowWidth, -1.0 + 2*yPos/windowHeight + 2*ySize/windowHeight, 0)
    glTexCoord2f(sprite.maxX, sprite.minY)
    glVertex3d(-1.0 + 2*xPos/windowWidth, -1.0 + 2*yPos/windowHeight + 2*ySize/windowHeight, 0)
    glEnd()
    glDisable(GL_BLEND)
  }

  def RectG(gridX: Float, gridY: Float, gridXUnit: Float, gridYUnit: Float, tile: Drawable): Unit = {
    val x = gridX
    val y = gridY
    val xu = gridXUnit
    val yu = gridYUnit
    tile match {
      case c@Color(_, _, _, _) => Rect(x*xu, y*yu, xu, yu, c)
      case t@Texture(_) => Rect(x*xu, y*yu, xu, yu, t)
      case s@Sprite(_, _, _, _, _) => Rect(x*xu, y*yu, xu, yu, s)
      case _ =>
    }
  }

  def renderArrayFill(array: Array[Array[Int]], f: Map[Int, Drawable]): Unit = { //todo: inner arrays have to all be the same length
    gridXSize = windowWidth.toFloat/(array.length-2)
    gridYSize = windowHeight.toFloat/(array(0).length-2)
    for (x <- 0 until array.length-1; y <- 1 until array(x).length-1) {
      RectG(x-1,y-1,gridXSize, gridYSize, f(array(x)(y)))
    }
  }

  def renderArrayFill(array: Array[Array[Int]], f: Map[Int, Drawable], xOffset: Float, yOffset: Float): Unit = {
    gridXSize = windowWidth.toFloat/(array.length-2)
    gridYSize = windowHeight.toFloat/(array(0).length-2)
    for (x <- 0 until array.length; y <- 0 until array(x).length) {
      RectG(x-1+xOffset,y-1+yOffset,gridXSize, gridYSize, f(array(x)(y)))
    }
  }

  def centerDiamond(): Unit = {
    glColor3f(1,0,1)
    glBegin(GL_POLYGON)
    glVertex3d(0, -gridYSize/windowHeight, 0)
    glVertex3d(-gridXSize/windowWidth, 0, 0)
    glVertex3d(0, gridYSize/windowHeight, 0)
    glVertex3d(gridXSize/windowWidth, 0, 0)
    glEnd()
  }

  def centerSprite(sprite: Sprite): Unit = {
    Rect((windowWidth-gridXSize)/2,(windowHeight-gridYSize)/2,gridXSize,gridYSize,sprite)
  }
}
