import org.lwjgl.opengl.GL11._

object Render {

  //todo: put this somewhere else, cant be global
  var gridXSize: Float = 0
  var gridYSize: Float = 0
  val windowWidth: Int = Config.windowSize.xi
  val windowHeight: Int = Config.windowSize.yi

  def RectG(gridX: Float, gridY: Float, gridXUnit: Float, gridYUnit: Float, tile: Drawable): Unit = {
    val x = gridX
    val y = gridY
    val xu = gridXUnit
    val yu = gridYUnit
    tile.drawRectRelative(Vector2(x*xu, y*yu), Vector2(xu, yu), Vector2(windowWidth.toFloat*2f, windowHeight.toFloat*2f))
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
    println(gridXSize)
  }

  def centerSprite(sprite: Sprite): Unit = {
    sprite.drawRectRelative(Vector2((windowWidth-gridXSize)/2,(windowHeight-gridYSize)/2),Vector2(gridXSize,gridYSize))
  }
}
