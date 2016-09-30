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
      -0.5f,  0.5f, 0.0f,
      -0.5f, -0.5f, 0.0f,
      0.5f, -0.5f, 0.0f,
      0.5f,  0.5f, 0.0f
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
    val square = new GameObject(mesh)
    square.position.z = -1.0f
    square
  }
  private val gameObjects = Array(square)

  def update(window: Window, interval: Float): Unit = {
    // Position
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
    if (window.isKeyPressed(GLFW_KEY_Q)) {
      square.position.z += 0.01f
    }
    if (window.isKeyPressed(GLFW_KEY_E)) {
      square.position.z -= 0.01f
    }
    // Rotation
    if (window.isKeyPressed(GLFW_KEY_I)) {
      square.rotation.x += 1.0f
    }
    if (window.isKeyPressed(GLFW_KEY_K)) {
      square.rotation.x -= 1.0f
    }
    if (window.isKeyPressed(GLFW_KEY_J)) {
      square.rotation.y += 1.0f
    }
    if (window.isKeyPressed(GLFW_KEY_L)) {
      square.rotation.y -= 1.0f
    }
    if (window.isKeyPressed(GLFW_KEY_U)) {
      square.rotation.z += 1.0f
    }
    if (window.isKeyPressed(GLFW_KEY_O)) {
      square.rotation.z -= 1.0f
    }
    // Scale
    if (window.isKeyPressed(GLFW_KEY_R)) {
      square.scale *= 1.01f
    }
    if (window.isKeyPressed(GLFW_KEY_F)) {
      square.scale *= 0.99f
    }
    if (window.isKeyPressed(GLFW_KEY_SPACE)) {
      square.reset()
      square.position.z = -1.0f
    }
  }

  def render(window: Window): Unit = {
    renderer.render(window, gameObjects)
  }

  def cleanup(): Unit = {
    renderer.cleanup()
  }
}
