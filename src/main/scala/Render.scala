import org.lwjgl.opengl.GL11._

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

  def Square(gridX: Int, gridY: Int, gridUnit: Int, color: Color): Unit = {
    val x = gridX.toFloat
    val y = gridY.toFloat
    val u = gridUnit.toFloat
    Square(x*u, y*u, u, color)
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

  def RectG(gridX: Int, gridY: Int, gridXUnit: Float, gridYUnit: Float, color: Color): Unit = {
    val x = gridX.toFloat
    val y = gridY.toFloat
    val xu = gridXUnit.toFloat
    val yu = gridYUnit.toFloat
    Rect(x*xu, y*yu, xu, yu, color)
  }

  def centerDiamond(): Unit = {
    glColor3f(1,0,1)
    glBegin(GL_POLYGON)
    glVertex3d(0, -gridYSize.toFloat/height, 0)
    glVertex3d(-gridXSize.toFloat/width, 0, 0)
    glVertex3d(0, gridYSize.toFloat/height, 0)
    glVertex3d(gridXSize.toFloat/width, 0, 0)

    glEnd()
  }

  def ChessBoard(): Unit = {
    for (x <- Range(0,8); y <- Range(0,8)) {
      if ((x+y) % 2 == 0) {
        Render.Square(x, y, width/8, Color.Black)
      }
    }
  }

  def Epilepsy(): Unit = {
    var color: Color = Color.White
    for (x <- Range(0,8); y <- Range(0,8)) {
      color = Color.simpleMap((Math.random()*5).toInt)
      Render.Square(x, y, width/8, color)
    }
  }

  def renderArrayFill(array: Array[Array[Int]], f: Map[Int, Color]): Unit = { //todo: inner arrays have to all be the same length
    gridXSize = width.toFloat/array.length
    gridYSize = height.toFloat/array(0).length
    for (x <- array.indices; y <- array(x).indices) {
      RectG(x,y,gridXSize, gridYSize, f(array(x)(y)))
    }
  }

}
