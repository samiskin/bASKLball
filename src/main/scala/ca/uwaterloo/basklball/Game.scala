package ca.uwaterloo.basklball

import org.lwjgl.glfw.GLFW._

object Game {
  val WIDTH  = 800
  val HEIGHT = 600
}

class Game {
  private val renderer = new Renderer()

  // A square
  private val square = {
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
    val mesh = new Mesh(positions, colors, indices)
    new GameObject(mesh)
  }
  private val gameObjects = Array(square)

  def update(window: Window, interval: Float): Unit = {
    if (window.isKeyPressed(GLFW_KEY_W)) {
      square.position.y += 0.01f
    }
    if (window.isKeyPressed(GLFW_KEY_A)) {
      square.position.x -= 0.01f
    }
    if (window.isKeyPressed(GLFW_KEY_S)) {
      square.position.y -= 0.01f
    }
    if (window.isKeyPressed(GLFW_KEY_D)) {
      square.position.x += 0.01f
    }
  }

  def render(window: Window): Unit = {
    renderer.render(window, gameObjects)
  }

  def cleanup(): Unit = {
    renderer.cleanup()
  }
}
