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

class Window { // The window handle
  private var window = 0L
  var level: Array[Array[Int]] = Level.RandomLevel(100,100, 5)
  var levelSlice: Array[Array[Int]] = level
  var p = PlayerState(50,50, 1)
  var textures: Map[Int, Tile.Texture] = Map.empty
  var grassTexPath: String = "src/resources/grass03.png"

  def run(): Unit = {
    init()
    loop()
    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(window)
    glfwDestroyWindow(window)
    // Terminate GLFW and free the error callback
    glfwTerminate()
    glfwSetErrorCallback(null).free()
  }

  private def init(): Unit = { // Setup an error callback. The default implementation
    // will print the error message in System.err.
    GLFWErrorCallback.createPrint(System.err).set
    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if (!glfwInit) throw new IllegalStateException("Unable to initialize GLFW")
    // Configure GLFW
    glfwDefaultWindowHints() // optional, the current window hints are already the default

    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation

    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable

    // Create the window
    window = glfwCreateWindow(Render.width, Render.height, "GridEngine", NULL, NULL)
    if (window == NULL) throw new RuntimeException("Failed to create the GLFW window")
    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window, (window: Long, key: Int, scancode: Int, action: Int, mods: Int) => {
      def foo(window: Long, key: Int, scancode: Int, action: Int, mods: Int) = {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) glfwSetWindowShouldClose(window, true) // We will detect this in the rendering loop
        else if (action == GLFW_PRESS || action == GLFW_REPEAT) {
          println(key)
          p = p.control(Input(key))
        }
      }

      foo(window, key, scancode, action, mods)
    })
    // Get the thread stack and push a new frame
    try {
      val stack = stackPush
      try {
        val pWidth = stack.mallocInt(1) // int*
        val pHeight = stack.mallocInt(1)
        // Get the window size passed to glfwCreateWindow
        glfwGetWindowSize(window, pWidth, pHeight)
        // Get the resolution of the primary monitor
        val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor)
        // Center the window
        glfwSetWindowPos(window, (vidmode.width - pWidth.get(0)) / 2, (vidmode.height - pHeight.get(0)) / 2)
        // the stack frame is popped automatically} finally {
        if (stack != null) stack.close()
      }
    }
    // Make the OpenGL context current
    glfwMakeContextCurrent(window)
    // Enable v-sync
    glfwSwapInterval(1)
    // Make the window visible
    glfwShowWindow(window)


  }

  private def loop(): Unit = { // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities
    // Set the clear color
    glClearColor(1.0f, 1.0f, 1.0f, 0.0f)
    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.

    val grassTex = TextureLoad(grassTexPath)
    textures = Tile.Texture.OpenPathSet()

    while ( {
      !glfwWindowShouldClose(window)
    }) {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT) // clear the framebuffer

      levelSlice = Level.getSlice(level, p.zoom*2+1, p.zoom*2+1,(p.xPos,p.yPos))
      //Render.renderArrayFill(levelSlice, Tile.Color.simpleMap)
      Render.renderArrayFill(levelSlice, textures)
      Render.centerDiamond()

      glfwSwapBuffers(window) // swap the color buffers

      // Poll for window events. The key callback above will only be
      // invoked during this call.
      glfwPollEvents()
    }
  }
}