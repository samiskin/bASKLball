package ca.uwaterloo.basklball

import org.lwjgl.glfw.GLFW._

object Game {
  val WIDTH  = 800
  val HEIGHT = 600
}

class Game {
  private val renderer = new Renderer()

  // A cube despite the name
  private val ball = {
    // x,y,z coordinates
    val positions = Array(
      -0.5f,  0.5f,  0.5f,
      -0.5f, -0.5f,  0.5f,
      0.5f, -0.5f,  0.5f,
      0.5f,  0.5f,  0.5f,
      -0.5f,  0.5f, -0.5f,
      0.5f,  0.5f, -0.5f,
      -0.5f, -0.5f, -0.5f,
      0.5f, -0.5f, -0.5f
    )
    // color corresponding to points in positions
    val colors = Array(
      0.5f, 0.0f, 0.0f,
      0.0f, 0.5f, 0.0f,
      0.0f, 0.0f, 0.5f,
      0.0f, 0.5f, 0.5f,
      0.5f, 0.0f, 0.0f,
      0.0f, 0.5f, 0.0f,
      0.0f, 0.0f, 0.5f,
      0.0f, 0.5f, 0.5f
    )
    // Each element in this array defines a vertex. The attrs of the vertex are looked up in the
    // previous matrices
    val indices = Array(
      0, 1, 3, 3, 1, 2, // front
      4, 0, 3, 5, 4, 3, // top
      3, 2, 7, 5, 3, 7, // right
      0, 1, 6, 4, 0, 6, // left
      6, 1, 2, 7, 6, 2, // bottom
      4, 6, 7, 5, 4, 7  // back
    )
    val mesh = new Mesh(positions, colors, indices)
    val cube = new GameObject(mesh)
    cube.position.z = -2.0f
    cube
  }
  private val gameObjects = Array(ball)

  def update(window: Window, interval: Float): Unit = {
    // Position
    if (window.isKeyPressed(GLFW_KEY_W)) {
      ball.position.y += 0.01f
    }
    if (window.isKeyPressed(GLFW_KEY_A)) {
      ball.position.x -= 0.01f
    }
    if (window.isKeyPressed(GLFW_KEY_S)) {
      ball.position.y -= 0.01f
    }
    if (window.isKeyPressed(GLFW_KEY_D)) {
      ball.position.x += 0.01f
    }
    if (window.isKeyPressed(GLFW_KEY_Q)) {
      ball.position.z += 0.01f
    }
    if (window.isKeyPressed(GLFW_KEY_E)) {
      ball.position.z -= 0.01f
    }
    // Rotation
    if (window.isKeyPressed(GLFW_KEY_I)) {
      ball.rotation.x += 1.0f
    }
    if (window.isKeyPressed(GLFW_KEY_K)) {
      ball.rotation.x -= 1.0f
    }
    if (window.isKeyPressed(GLFW_KEY_J)) {
      ball.rotation.y += 1.0f
    }
    if (window.isKeyPressed(GLFW_KEY_L)) {
      ball.rotation.y -= 1.0f
    }
    if (window.isKeyPressed(GLFW_KEY_U)) {
      ball.rotation.z += 1.0f
    }
    if (window.isKeyPressed(GLFW_KEY_O)) {
      ball.rotation.z -= 1.0f
    }
    // Scale
    if (window.isKeyPressed(GLFW_KEY_R)) {
      ball.scale *= 1.01f
    }
    if (window.isKeyPressed(GLFW_KEY_F)) {
      ball.scale *= 0.99f
    }
    if (window.isKeyPressed(GLFW_KEY_SPACE)) {
      ball.reset()
      ball.position.z = -1.0f
    }
  }

  def render(window: Window): Unit = {
    renderer.render(window, gameObjects)
  }

  def cleanup(): Unit = {
    renderer.cleanup()
  }
}
