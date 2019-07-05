import Input._
import org.lwjgl.glfw._
import org.lwjgl.opengl._
import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL11._
import org.lwjgl.system.MemoryStack._
import org.lwjgl.system.MemoryUtil._

object Window {
  def main(args: Array[String]): Unit = { //todo: make proper main that can launch (different) windows. Game, leveleditor etc
    new Window().run()
  }
}

class Window {
  private var window = 0L

  var lastInput: Input = None
  var lastTime: Long = 0

  def run(): Unit = {
    init()
    loop()

    glfwFreeCallbacks(window)
    glfwDestroyWindow(window)

    glfwTerminate()
    glfwSetErrorCallback(null).free()
  }

  private def init(): Unit = {
    GLFWErrorCallback.createPrint(System.err).set

    if (!glfwInit) throw new IllegalStateException("Unable to initialize GLFW")

    glfwDefaultWindowHints()

    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
    glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE)

    window = glfwCreateWindow(Config.windowSize.xi, Config.windowSize.yi, Config.windowName, NULL, NULL)
    if (window == NULL) throw new RuntimeException("Failed to create the GLFW window")

    glfwSetKeyCallback(window, (window: Long, key: Int, scancode: Int, action: Int, mods: Int) => {
      // todo: make better input
      def foo(window: Long, key: Int, scancode: Int, action: Int, mods: Int): Unit = {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
          glfwSetWindowShouldClose(window, true)
        } else if (action == GLFW_PRESS || action == GLFW_REPEAT) {
          lastInput = Input(key)
        }
      }

      foo(window, key, scancode, action, mods)
    })
    try {
      val stack = stackPush
      try {
        val pWidth = stack.mallocInt(1)
        val pHeight = stack.mallocInt(1)

        glfwGetWindowSize(window, pWidth, pHeight)
        val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor)
        glfwSetWindowPos(window, (vidmode.width - pWidth.get(0)) / 2, (vidmode.height - pHeight.get(0)) / 2)
      }
      finally {
        if (stack != null) stack.close()
      }
    }

    glfwMakeContextCurrent(window)
    glfwSwapInterval(1)
    glfwShowWindow(window)
  }

  private def loop(): Unit = {
    GL.createCapabilities
    glClearColor(1.0f, 1.0f, 1.0f, 0.0f)

    var test = Array.ofDim[Int](21,21)
    for (x <- test.indices; y <- test(x).indices) {
      test(x)(y) = (Math.random()*16).toInt
    }
    test = new Level(test).getSlice(test.length+2, test(0).length+2, (10,10))

    val testMap = Sprite.get("src/resources/Tileset/basictiles.png", Vector2(128, 240), Vector2(16, 16))
    val testFont = Sprite.get("src/resources/Font/8x8Text/8x8text_darkGrayShadow.png", Vector2(96, 112), Vector2(8))

    val testText = "  HELLO WORLD!  ".toCharArray.map(Text.CharToInt(_)).map(x => Array(167,167,167,167,167,167,167,167, x, 167,167,167,167,167,167,167,167))

    val testTile = Tile(List(testMap(11), testMap(56), testMap(24), testMap(29)))



    val tiles = Array.ofDim[Tile](11,11)
    for (x <- tiles.indices; y <- tiles(x).indices) {
      tiles(x)(y) = testTile
    }

    var counter: Float = 0
    var scrollSpeed: Float = 128
    var up = true
    val g = new Grid
    val g1 = new Grid(relativeSize = Vector2(0.5f))
    val g2 = new Grid(relativeSize = Vector2(0.5f), relativePosition = Vector2(0.5f,0))
    val g3 = new Grid(relativeSize = Vector2(0.5f), relativePosition = Vector2(0,0.5f))
    val g4 = new Grid(relativeSize = Vector2(0.5f), relativePosition = Vector2(0.5f))
    var state = Overworld.testWorld(Level.TestDungeon)

    val formatTest = LevelFormat.pureToDraw(LevelFormat.testPure)
    for(x <- 0 until 3; y <- 0 until 3) {
      println(formatTest(x)(y) + " " + LevelFormat.testDraw(x)(y))
    }

    val text = Text("HELLO WORLD!", Text.GrayFont)
    val t1 = Text("Trait Drawable", Text.GrayFont)
    val t2 = Text("hat sich bewiesen!", Text.GrayFont)
    val t3 = Text("40min fuer volle Kontrolle", Text.GrayFont)
    val t4 = Text("und Integration mit Grid!", Text.GrayFont)

    while ( { //todo: cleanup in preparation for menu neccessary
      !glfwWindowShouldClose(window)
    }) {
      val thisTime = System.currentTimeMillis() //todo: wrap this in a function or something
      val deltaTime = thisTime - lastTime

      if (deltaTime > 1000f / Config.fps) {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
        state = state.simulate(deltaTime, lastInput)

        //g1.drawGrid(testText, testFont, Vector2(0))
        //g2.drawGrid(test, testMap, Vector2(0))
        g3.drawGrid(test, testMap, Vector2(counter/scrollSpeed, 0))
        //g4.drawGrid(test, testMap, Vector2(0, counter/scrollSpeed))

        g.drawGrid(test, testMap, Vector2(0))
        g.drawOnGrid(t1, Vector2(8,15), Vector2(0), Vector2(t1.string.length))
        g.drawOnGrid(t2, Vector2(4,14), Vector2(0), Vector2(t2.string.length))
        g.drawOnGrid(t3, Vector2(4,11), Vector2(0.5f,0), Vector2(t3.string.length/2))
        g.drawOnGrid(t4, Vector2(2,8), Vector2(0, -0.5f), Vector2(t4.string.length/2))
        g.drawOnGrid(Color.White, Vector2(2), Vector2(0))

        state.render(g)


        //text.drawRectangle(Vector2(0.5f), Vector2(0.25f, 0.5f))
        //testMap(0).drawRectangle(Vector2(0.5f, 0.5f), Vector2(0.5f, 0.5f))

        //g.drawGrid(testText, testFont, Vector2(0))

        //g1.drawGridScrollable(tiles, Vector2(counter/scrollSpeed,0))
        //state.render(g2)
        //g3.drawGridScrollable(formatTest, Vector2(0))
        //g4.drawGridScrollable(test, testFont, Vector2(0))



        counter = if (up) {
          if (counter < scrollSpeed) {
            counter + 1
          } else {
            up = false
            scrollSpeed
          }
        } else {
          if (counter > -scrollSpeed) {
            counter - 1
          } else {
            up = true
            -scrollSpeed
          }
        }

        lastTime = thisTime
        lastInput = None
      }

      glfwSwapBuffers(window)

      glfwPollEvents()
    }
  }
}