import org.lwjgl.glfw._
import org.lwjgl.opengl._
import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL11._
import org.lwjgl.system.MemoryStack._
import org.lwjgl.system.MemoryUtil._

class Window(initState: () => Stateful) {
  var lastInput: InputKey = InputKey.NoKey
  var lastCursor: Vector2 = Vector2(0)
  var lastMouse: InputMouseButton = InputMouseButton.NoMouseButton
  var lastTime: Long = 0

  private var state: Option[Stateful] = None
  private var window = 0L

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
      Input.moveCursor(Vector2(xPos.toFloat, Config.windowSize.y-yPos.toFloat))
    }

    def mouseCallback(window: Long, button: Int, action: Int, mods: Int): Unit = {
      if (action == GLFW_PRESS) {
        Input.addButton(InputMouseButton(button))
      } else if (action == GLFW_RELEASE) {
        Input.removeButton(InputMouseButton(button))
      }
    }

    def keyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int): Unit = {

      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
        glfwSetWindowShouldClose(window, true)
      } else if (action == GLFW_PRESS || action == GLFW_REPEAT) {
        Input.addKey(InputKey(key))
      } else if (action == GLFW_RELEASE) {
        Input.removeKey(InputKey(key))
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
    state = Some(initState())

    while ( {
      !glfwWindowShouldClose(window)
    }) {
      val thisTime = System.currentTimeMillis() //todo: wrap this in a function or something
      val deltaTime = thisTime - lastTime

      if (deltaTime > 1000f / Config.fps) {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)


        state = state.map(_.simulate(deltaTime, lastInput))
        state.foreach(_.render())
        lastTime = thisTime
        lastInput = InputKey.NoKey

        println(Input.update)

        //println(lastCursor)
        //println(lastMouse)


      }

      glfwSwapBuffers(window)

      glfwPollEvents()
    }
  }
}