import org.lwjgl.opengl.GL11._
import Tile._

object Render {

  val width = 800
  val height = 800

  var gridXSize: Float = 0
  var gridYSize: Float = 0

  def Square(xPos: Float, yPos: Float, size: Float, color: Color): Unit = {
    glColor3f(color.red, color.green, color.blue)
    glBegin(GL_POLYGON)
    glVertex3d(-1.0 + 2*xPos/width, -1.0 + 2*yPos/height, 0)
    glVertex3d(-1.0 + 2*xPos/width + 2*size/width, -1.0 + 2*yPos/height, 0)
    glVertex3d(-1.0 + 2*xPos/width + 2*size/width, -1.0 + 2*yPos/height + 2*size/height, 0)
    glVertex3d(-1.0 + 2*xPos/width, -1.0 + 2*yPos/height + 2*size/height, 0)
    glEnd()
  }

  def Square(xPos: Float, yPos: Float, size: Float, texture: Texture): Unit = {
    glColor3f(1f,1f,1f)
    glBegin(GL_POLYGON)
    glTexCoord2f(0, 0)
    glVertex3d(-1.0 + 2*xPos/width, -1.0 + 2*yPos/height, 0)
    glTexCoord2f(1, 0)
    glVertex3d(-1.0 + 2*xPos/width + 2*size/width, -1.0 + 2*yPos/height, 0)
    glTexCoord2f(1, 1)
    glVertex3d(-1.0 + 2*xPos/width + 2*size/width, -1.0 + 2*yPos/height + 2*size/height, 0)
    glTexCoord2f(0, 1)
    glVertex3d(-1.0 + 2*xPos/width, -1.0 + 2*yPos/height + 2*size/height, 0)
    glEnd()
  }

  def SquareG(gridX: Int, gridY: Int, gridUnit: Int, tile: Tile): Unit = {
    val x = gridX.toFloat
    val y = gridY.toFloat
    val u = gridUnit.toFloat
    tile match {
      case c@Color(_, _, _) => Square(x*u, y*u, u, c)
      case t@Texture(_) => Square(x*u, y*u, u, t)
    }
  }

  def Rect(xPos: Float, yPos: Float, xSize: Float, ySize: Float, color: Color): Unit = {
    glColor3f(color.red, color.green, color.blue)
    glBegin(GL_POLYGON)
    glVertex3d(-1.0 + 2*xPos/width, -1.0 + 2*yPos/height, 0)
    glVertex3d(-1.0 + 2*xPos/width + 2*xSize/width, -1.0 + 2*yPos/height, 0)
    glVertex3d(-1.0 + 2*xPos/width + 2*xSize/width, -1.0 + 2*yPos/height + 2*ySize/height, 0)
    glVertex3d(-1.0 + 2*xPos/width, -1.0 + 2*yPos/height + 2*ySize/height, 0)
    glEnd()
  }

  def Rect(xPos: Float, yPos: Float, xSize: Float, ySize: Float, tex: Texture): Unit = {
    TextureLoad.bind(tex.id)
    glColor3f(1f,1f,1f)
    glBegin(GL_POLYGON)
    glTexCoord2f(0, 0)
    glVertex3d(-1.0 + 2*xPos/width, -1.0 + 2*yPos/height, 0)
    glTexCoord2f(1, 0)
    glVertex3d(-1.0 + 2*xPos/width + 2*xSize/width, -1.0 + 2*yPos/height, 0)
    glTexCoord2f(1, 1)
    glVertex3d(-1.0 + 2*xPos/width + 2*xSize/width, -1.0 + 2*yPos/height + 2*ySize/height, 0)
    glTexCoord2f(0, 1)
    glVertex3d(-1.0 + 2*xPos/width, -1.0 + 2*yPos/height + 2*ySize/height, 0)
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
    val x = gridXSize/width
    val y = gridYSize/height
    glBegin(GL_POLYGON)
    glTexCoord2f(u2, v2)
    glVertex3d(xPos-x, yPos-y, 0)
    glTexCoord2f(u, v2)
    glVertex3d(xPos+x, yPos-y, 0)
    glTexCoord2f(u, v)
    glVertex3d(yPos+x, yPos+y, 0)
    glTexCoord2f(u2, v)
    glVertex3d(xPos-x, yPos+y, 0)
    glEnd()

    glDisable(GL_BLEND)
  }

  def RectG(gridX: Int, gridY: Int, gridXUnit: Float, gridYUnit: Float, tile: Tile): Unit = {
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
    gridXSize = width.toFloat/array.length
    gridYSize = height.toFloat/array(0).length
    for (x <- array.indices; y <- array(x).indices) {
      RectG(x,y,gridXSize, gridYSize, f(array(x)(y)))
    }
  }

  def centerDiamond(): Unit = {
    glColor3f(1,0,1)
    glBegin(GL_POLYGON)
    glVertex3d(0, -gridYSize/height, 0)
    glVertex3d(-gridXSize/width, 0, 0)
    glVertex3d(0, gridYSize/height, 0)
    glVertex3d(gridXSize/width, 0, 0)
    glEnd()
  }

  def centerSprite(sprite: Sprite): Unit = { //todo: make direction a thing, again. Use spritesheets.
    Rect(0,0,gridXSize/width,gridYSize/height,sprite)
  }

  //for fun test stuff begins here

  def ChessBoard(): Unit = {
    for (x <- Range(0,8); y <- Range(0,8)) {
      if ((x+y) % 2 == 0) {
        Render.SquareG(x, y, width/8, Color.Black)
      }
    }
  }

  def textureChessBoard(tex: Texture): Unit = {
    for (x <- Range(0,8); y <- Range(0,8)) {
      if ((x+y) % 2 == 0) {
        Render.RectG(x, y, Render.width/8, Render.height/8, tex)
      }
    }
  }

  def Epilepsy(): Unit = {
    var color: Color = Color.White
    for (x <- Range(0,8); y <- Range(0,8)) {
      color = Color.simpleMap((Math.random()*5).toInt)
      Render.SquareG(x, y, width/8, color)
    }
  }

  def textureFill(tex: Texture): Unit = {
    TextureLoad.bind(tex.id)
    glColor3f(1,1,1)
    glBegin(GL_POLYGON)
    glTexCoord2f(0, 0)
    glVertex2f(-1, -1)
    glTexCoord2f(0, 1)
    glVertex2f(-1, 1)
    glTexCoord2f(1, 1)
    glVertex2f(1, 1)
    glTexCoord2f(1, 0)
    glVertex2f(1, -1)
    glEnd()
  }

  def tileTexture(tex: Texture): Unit = {
    for (x <- Range(0,8); y <- Range(0,8)) {
      Render.RectG(x, y, Render.width/8, Render.height/8, tex)
    }
  }

}
