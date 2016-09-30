package ca.uwaterloo.basklball

import org.lwjgl.glfw.GLFW._

class Game {
  private val mesh = {
    val positions = Array(
      -0.5f,  0.5f, 0.0f,
      -0.5f, -0.5f, 0.0f,
       0.5f, -0.5f, 0.0f,
       0.5f,  0.5f, 0.0f
    )
    val colors = Array(
      0.5f, 0.0f, 0.0f,
      0.0f, 0.5f, 0.0f,
      0.0f, 0.0f, 0.5f,
      0.0f, 0.5f, 0.5f
    )
    val indices = Array(0, 1, 3, 3, 1, 2)
    new Mesh(positions, colors, indices)
  }
  private val renderer = new Renderer()

  private var xOffset = 0.0f
  private var yOffset = 0.0f

  def update(window: Window, interval: Float): Unit = {
    if (window.isKeyPressed(GLFW_KEY_W)) {
      yOffset += 0.01f
    }
    if (window.isKeyPressed(GLFW_KEY_A)) {
      xOffset -= 0.01f
    }
    if (window.isKeyPressed(GLFW_KEY_S)) {
      yOffset -= 0.01f
    }
    if (window.isKeyPressed(GLFW_KEY_D)) {
      xOffset += 0.01f
    }
  }

  def render(window: Window): Unit = {
    renderer.render(window, mesh)
  }

  def cleanup(): Unit = {
    renderer.cleanup()
  }
}
