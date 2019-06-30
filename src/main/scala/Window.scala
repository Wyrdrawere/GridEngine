import Input._
import org.lwjgl._
import org.lwjgl.glfw._
import org.lwjgl.opengl._
import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL11._
import org.lwjgl.system.MemoryStack._
import org.lwjgl.system.MemoryUtil._

object Window {
  def main(args: Array[String]): Unit = {
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
        } else if(action == GLFW_PRESS || action == GLFW_REPEAT) {
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

    //var state = Overworld.testWorld(Level.TestDungeon)

    val test = Array.ofDim[Int](11,11)
    for(x <- test.indices; y <- test(x).indices) {
      test(x)(y) = (Math.random()*16).toInt
    }

    val test2 = Array.ofDim[Int](21,11)
    for(x <- test2.indices; y <- test2(x).indices) {
      test2(x)(y) = if((x+y)%2 == 0) 9 else 11

    }

    val test3 = Array.ofDim[Int](21,11)
    for(x <- test3.indices; y <- test3(x).indices) {
      test3(x)(y) = 13
    }

    var testMap = Sprite.TextureToTileSet(Texture.load("src/resources/Tileset/basictiles.png"),128,240,16,16)
    var testMap2 = Sprite.TextureToTileSet(Texture.load("src/resources/Sprite/ff1-classes.png"), 672, 432, 36, 36)

    val g = new Grid(relativeSize = Vector2(0.5f), relativePosition = Vector2(0.25f,0.25f))
    val g2 = new Grid
    val g3 = new Grid(relativeSize = Vector2(0.5f), relativePosition = Vector2(0))
    val g4 = new Grid(relativeSize = Vector2(0.5f), relativePosition = Vector2(0, 0.5f))
    val g5 = new Grid(relativeSize = Vector2(0.5f), relativePosition = Vector2(0.5f))
    val g6 = new Grid(relativeSize = Vector2(0.5f), relativePosition = Vector2(0.5f, 0))

    var state = Overworld.testWorld(Level.TestDungeon)

    var counter: Float = 0

    val scrollSpeed:Float=128

    while ( {
      !glfwWindowShouldClose(window)
    }) {


      val thisTime = System.currentTimeMillis() //todo: wrap this in a function or something
      val deltaTime = thisTime-lastTime


      if (deltaTime > 1000f/Config.fps) {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
        state = state.simulate(deltaTime, lastInput)
        println(counter)
        if (counter < scrollSpeed) (counter = counter + 1) else (counter = 0)

        g3.drawGrid(test,testMap, offset = Vector2(0,+counter/scrollSpeed))
        g4.drawGrid(test,testMap, offset = Vector2(0,-counter/scrollSpeed))
        g5.drawGrid(test,testMap, offset = Vector2(+counter/scrollSpeed,0))
        g6.drawGrid(test,testMap, offset = Vector2(-counter/scrollSpeed,0))

        if (counter < scrollSpeed) (counter = counter + 1) else counter = 0

        lastTime = thisTime
        lastInput = None
      }







      glfwSwapBuffers(window)

      glfwPollEvents()
    }
  }
}