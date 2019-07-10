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

    val g = new Grid
    val g1 = new Grid(relativeSize = Vector2(0.5f))
    val g2 = new Grid(relativeSize = Vector2(0.5f, 1), relativePosition = Vector2(0.5f,0))
    val g3 = new Grid(relativeSize = Vector2(0.5f), relativePosition = Vector2(0,0.5f))
    val g4 = new Grid(relativeSize = Vector2(0.5f), relativePosition = Vector2(0.5f))

    val text = Text("HELLO WORLD!", Text.DarkGrayFont)
    val t1 = Text("STATUS", Text.DarkGrayFont)
    val t2 = Text("SKILLS", Text.DarkGrayFont)
    val t3 = Text("EQUIPMENT", Text.DarkGrayFont)
    val t4 = Text("JOBS", Text.DarkGrayFont)

    val testList: List[Text] = List(t1,t2,t3,t4)

    var state = ListMenu(0, testList)

    while ( {
      !glfwWindowShouldClose(window)
    }) {
      val thisTime = System.currentTimeMillis() //todo: wrap this in a function or something
      val deltaTime = thisTime - lastTime

      if (deltaTime > 1000f / Config.fps) {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
        state = state.simulate(deltaTime, lastInput)

        g.drawGrid(test, testMap, Vector2(0))
        state.render(g2)


        lastTime = thisTime
        lastInput = None
      }

      glfwSwapBuffers(window)

      glfwPollEvents()
    }
  }
}