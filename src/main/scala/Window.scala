import java.nio.{ByteBuffer, IntBuffer}

import deprecate.Input
import drawables.Text
import engine.Entity
import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.glfw._
import org.lwjgl.openal.ALC10._
import org.lwjgl.openal.EXTThreadLocalContext.alcSetThreadContext
import org.lwjgl.openal.{AL, ALC}
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl._
import org.lwjgl.system.MemoryStack._
import org.lwjgl.system.MemoryUtil._
import render.Grid
import util.{Config, InputItem, Vector2}

object Window extends App {
  val window: Window = new Window()
  window.run()
}

class Window() {

  private var lastTime: Long = 0
  private var window: Long = 0L
  private var alContext: Long = 0L
  private var alDevice: Long = 0L

  def run(): Unit = {
    initGL()
    initAL()
    loop()
    closeAL()
    closeGL()
  }

  private def initGL(): Unit = {
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
      Input.cursor = Vector2(xPos, yPos)
    }

    def mouseCallback(window: Long, button: Int, action: Int, mods: Int): Unit = {
      if (action == GLFW_PRESS) {
        Input.add(InputItem.Mouse(button))
      } else if (action == GLFW_RELEASE) {
        Input.remove(InputItem.Mouse(button))
      }
    }

    def keyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int): Unit = {

      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
        glfwSetWindowShouldClose(window, true)
      } else if (action == GLFW_PRESS || action == GLFW_REPEAT) {
        Input.add(InputItem.Key(key))
      } else if (action == GLFW_RELEASE) {
        Input.remove(InputItem.Key(key))
      }
    }

    //glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN)

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

    glfwMakeContextCurrent(window)
    glfwSwapInterval(1)
    glfwShowWindow(window)
  }

  private def closeGL(): Unit = {
    glfwFreeCallbacks(window)
    glfwDestroyWindow(window)

    glfwTerminate()
    glfwSetErrorCallback(null).free()
  }

  private def initAL(): Unit = {
    alDevice = alcOpenDevice(null.asInstanceOf[ByteBuffer])
    if (alDevice == NULL) throw new IllegalStateException("Failed to open the default device.")
    val deviceCaps = ALC.createCapabilities(alDevice)
    alContext = alcCreateContext(alDevice, null.asInstanceOf[IntBuffer])
    alcSetThreadContext(alContext)
    AL.createCapabilities(deviceCaps)
  }

  private def closeAL(): Unit = {
    alcMakeContextCurrent(NULL)
    alcDestroyContext(alContext)
    alcCloseDevice(alDevice)
  }

  private def loop(): Unit = {

    GL.createCapabilities
    glClearColor(1.0f, 1.0f, 1.0f, 0.0f)

   lazy val test = sound.Sound.get("src/resources/Sound/REOL - No title.ogg")
   lazy val test2 = sound.Sound.get("src/resources/Sound/6 - (Don't Fear) The Reaper.ogg")

    val g = new Grid(Vector2(1),Vector2(0))

    var t = Text("DON'T FEAR THE REAPER", Text.DarkGrayFont)

    test.play()

    val e = new Entity {}

    e.attach(components.Health(15))
    println(e.has(components.Health))
    println(e.get(components.Health))
    e.detach(components.Health)
    println(e.has(components.Health))
    println(e.get(components.Health))
    e.attach(components.Health(-System.currentTimeMillis().toInt))

    while ( {
      !glfwWindowShouldClose(window)
    }) {
      val thisTime = System.currentTimeMillis() //todo: wrap this in a function or something
      val deltaTime = thisTime - lastTime

      if (deltaTime > 1000f / Config.fps) {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

        g.squareCells(5)
        g.drawOnGrid(drawables.Sprite.basicBackground(12), Vector2(1), Vector2(2))

        lastTime = thisTime
      }

      glfwSwapBuffers(window)

      glfwPollEvents()
    }

  }
}