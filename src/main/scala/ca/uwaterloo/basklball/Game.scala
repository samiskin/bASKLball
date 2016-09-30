package ca.uwaterloo.basklball

import org.lwjgl.glfw.GLFW._

object Game {
  val WIDTH  = 800
  val HEIGHT = 600
}

class Game {
  // A square
  private val mesh = {
    // x,y,z coordinates
    val positions = Array(
      -0.5f,  0.5f, -1.0f,
      -0.5f, -0.5f, -2.0f,
       0.5f, -0.5f, -3.0f,
       0.5f,  0.5f, -1.0f
    )
    // color corresponding to points in positions
    val colors = Array(
      0.5f, 0.0f, 0.0f,
      0.0f, 0.5f, 0.0f,
      0.0f, 0.0f, 0.5f,
      0.0f, 0.5f, 0.5f
    )
    // Each element in this array defines a vertex. The attrs of the vertex are looked up in the
    // previous matrices
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
