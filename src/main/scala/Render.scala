import Config.{windowWidth, windowHeight}
import org.lwjgl.opengl.GL11._
import Tile._

object Render {

  var gridXSize: Float = 0
  var gridYSize: Float = 0

  def Rect(xPos: Float, yPos: Float, xSize: Float, ySize: Float, color: Color): Unit = {
    glColor3f(color.red, color.green, color.blue)
    glBegin(GL_POLYGON)
    glVertex3d(-1.0 + 2*xPos/windowWidth, -1.0 + 2*yPos/windowHeight, 0)
    glVertex3d(-1.0 + 2*xPos/windowWidth + 2*xSize/windowWidth, -1.0 + 2*yPos/windowHeight, 0)
    glVertex3d(-1.0 + 2*xPos/windowWidth + 2*xSize/windowWidth, -1.0 + 2*yPos/windowHeight + 2*ySize/windowHeight, 0)
    glVertex3d(-1.0 + 2*xPos/windowWidth, -1.0 + 2*yPos/windowHeight + 2*ySize/windowHeight, 0)
    glEnd()
  }

  def Rect(xPos: Float, yPos: Float, xSize: Float, ySize: Float, tex: Texture): Unit = {
    TextureLoad.bind(tex.id)
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
    val u = sprite.minX
    val u2 = sprite.maxX
    val v = sprite.minY
    val v2 = sprite.maxY
    glEnable(GL_BLEND)
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
    TextureLoad.bind(sprite.id)
    glColor4f(1f,1f,1f,1f)
    val x = gridXSize/windowWidth
    val y = gridYSize/windowHeight
    glBegin(GL_POLYGON)
    glTexCoord2f(u2, v2)
    glVertex3d(-1.0 + 2*xPos/windowWidth, -1.0 + 2*yPos/windowHeight, 0)
    glTexCoord2f(u, v2)
    glVertex3d(-1.0 + 2*xPos/windowWidth + 2*xSize/windowWidth, -1.0 + 2*yPos/windowHeight, 0)
    glTexCoord2f(u, v)
    glVertex3d(-1.0 + 2*xPos/windowWidth + 2*xSize/windowWidth, -1.0 + 2*yPos/windowHeight + 2*ySize/windowHeight, 0)
    glTexCoord2f(u2, v)
    glVertex3d(-1.0 + 2*xPos/windowWidth, -1.0 + 2*yPos/windowHeight + 2*ySize/windowHeight, 0)
    glEnd()
    glDisable(GL_BLEND)
  }

  def RectG(gridX: Float, gridY: Float, gridXUnit: Float, gridYUnit: Float, tile: Tile): Unit = {
    val x = gridX.toFloat
    val y = gridY.toFloat
    val xu = gridXUnit.toFloat
    val yu = gridYUnit.toFloat
    tile match {
      case c@Color(_, _, _) => Rect(x*xu, y*yu, xu, yu, c)
      case t@Texture(_) => Rect(x*xu, y*yu, xu, yu, t)
      case s@Sprite(_, _, _, _, _) => Rect(x*xu, y*yu, xu, yu, s)
    }
  }

  def renderArrayFill(array: Array[Array[Int]], f: Map[Int, Tile]): Unit = { //todo: inner arrays have to all be the same length
    gridXSize = windowWidth.toFloat/array.length
    gridYSize = windowHeight.toFloat/array(0).length
    for (x <- array.indices; y <- array(x).indices) {
      RectG(x,y,gridXSize, gridYSize, f(array(x)(y)))
    }
  }

  def renderArrayFill(array: Array[Array[Int]], f: Map[Int, Tile], xOffset: Float, yOffset: Float): Unit = {
    gridXSize = windowWidth.toFloat/array.length
    gridYSize = windowHeight.toFloat/array(0).length
    for (x <- array.indices; y <- array(x).indices) {
      RectG(x+xOffset,y+yOffset,gridXSize, gridYSize, f(array(x)(y)))
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
