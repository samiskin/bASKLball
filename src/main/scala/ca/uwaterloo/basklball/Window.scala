package ca.uwaterloo.basklball

import org.lwjgl._, glfw._, opengl._
import Callbacks._, GLFW._, GL11._

import org.lwjgl.system.MemoryUtil._

class Window(title: String, width: Int, height: Int) {
  import CallbackHelpers._

  private val window = {
    glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err))

    if (!glfwInit()) {
      throw new IllegalStateException("Unable to initialize GLFW")
    }

    glfwDefaultWindowHints()
    glfwWindowHint(GLFW_VISIBLE,   GLFW_FALSE) // hiding the window
    glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE) // window resizing not allowed

    // Enable higher OpenGL version
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE)

    val window = glfwCreateWindow(width, height, title, NULL, NULL)
    if (window == NULL)
      throw new RuntimeException("Failed to create the GLFW window")

    glfwSetKeyCallback(window, (window: Long, key: Int, _: Int, action: Int, _: Int) => {
      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
        glfwSetWindowShouldClose(window, true)
      }
    })

    // Get resolution of monitor and center on screen
    val vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())
    glfwSetWindowPos(
      window,
      (vidMode.width() - width) / 2,
      (vidMode.height() - width) / 2
    )

    glfwMakeContextCurrent(window)
    glfwSwapInterval(1)
    glfwShowWindow(window)

    GL.createCapabilities()
    glClearColor(0f, 0f, 0f, 0f)

    window
  }

  def isKeyPressed(keyCode: Int) = glfwGetKey(window, keyCode) == GLFW_PRESS

  def windowShowClose() = glfwWindowShouldClose(window)

  def update(): Unit = {
    glfwSwapBuffers(window)
    glfwPollEvents()
  }

  def cleanup(): Unit = {
    glfwFreeCallbacks(window)
    glfwDestroyWindow(window)

    glfwTerminate()
    glfwSetErrorCallback(null).free()
  }
}
