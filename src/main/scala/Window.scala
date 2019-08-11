import Input._
import org.lwjgl.BufferUtils
import org.lwjgl.glfw._
import org.lwjgl.opengl._
import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL11._
import org.lwjgl.system.MemoryStack._
import org.lwjgl.system.MemoryUtil._

object Window {

  val currentWindow = new Window()

  def main(args: Array[String]): Unit = { //todo: make proper main that can launch (different) windows. Game, leveleditor etc
    currentWindow.run()
  }
}

class Window {
  private var window = 0L

  var lastInput: Input = None
  var lastCursor: Vector2 = Vector2(0)
  var lastMouse: Long = 0
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

    glfwSetCursorPosCallback(
      window,
      (window: Long, xPos: Double, yPos: Double) => cursorCallback(window, xPos, yPos))

    glfwSetMouseButtonCallback(
      window,
      (window: Long, button: Int, action: Int, mods: Int) => mouseCallback(window, button, action, mods))

    glfwSetKeyCallback(
      window,
      (window: Long, key: Int, scancode: Int, action: Int, mods: Int) => keyCallback(window, key, scancode, action, mods))

    def cursorCallback(window: Long, xPos: Double, yPos: Double): Unit = {
      lastCursor = Vector2(xPos.toFloat, Config.windowSize.y-yPos.toFloat)
    }

    def mouseCallback(window: Long, button: Int, action: Int, mods: Int): Unit = {
      if (action == GLFW_PRESS) {
        lastMouse = button
      }
    }

    def keyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int): Unit = {
      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
        glfwSetWindowShouldClose(window, true)
      } else if (action == GLFW_PRESS || action == GLFW_REPEAT) {
        lastInput = Input(key)
      }
    }


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

    var state: Stateful = new Overworld(
      Statebox.OverworldBox(
      Level.TestDungeon,
      OverworldSprite.FF1_PlayerSprite(0),
      Vector2(0),
      5,
      Scroller(Config.scrollUnit, Vector2(0), Scroller.Stay)
      ),
      new Grid)

    while ( {
      !glfwWindowShouldClose(window)
    }) {
      val thisTime = System.currentTimeMillis() //todo: wrap this in a function or something
      val deltaTime = thisTime - lastTime

      if (deltaTime > 1000f / Config.fps) {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)


        state = state.simulate(deltaTime, lastInput)
        state.render()
        lastTime = thisTime
        lastInput = None
        println(lastCursor)
        println(lastMouse)


      }

      glfwSwapBuffers(window)

      glfwPollEvents()
    }
  }
}