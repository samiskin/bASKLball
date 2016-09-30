package ca.uwaterloo.basklball

import org.lwjgl.glfw.GLFW._

object Game {
  val WIDTH  = 800
  val HEIGHT = 600
}

class Game {
  private val renderer = new Renderer()

  private val cubeMesh = {
    // x,y,z coordinates
    val positions = Array(
      -0.5f,  0.5f,  0.5f,
      -0.5f, -0.5f,  0.5f,
      0.5f, -0.5f,  0.5f,
      0.5f,  0.5f,  0.5f,
      -0.5f,  0.5f, -0.5f,
      0.5f,  0.5f, -0.5f,
      -0.5f, -0.5f, -0.5f,
      0.5f, -0.5f, -0.5f,
      -0.5f, -0.5f, -0.5f, // repeat vertices for proper texture
      0.5f, -0.5f, -0.5f
    )
    // corresponds to points in positions
    val textureCoordinates = Array(
      0.0f, 0.0f, // 0
      0.0f, 2.0f, // 1
      2.0f, 2.0f, // 2
      2.0f, 0.0f, // 3
      0.0f, 2.0f, // 4
      0.0f, 0.0f, // 2
      2.0f, 2.0f, // 6
      0.0f, 2.0f, // 7
      0.0f, 0.0f, // 8
      2.0f, 0.0f  // 9
    )
    // Each element in this array defines a vertex. The attrs of the vertex are looked up in the
    // previous matrices
    val indices = Array(
      0, 1, 3, 3, 1, 2, // front
      4, 0, 3, 5, 4, 3, // top
      3, 2, 7, 5, 3, 7, // right
      0, 1, 6, 4, 0, 6, // left
      8, 1, 2, 9, 8, 2, // bottom
      4, 8, 9, 5, 4, 9  // back
    )
    val texture = new Texture("/textures/basketball512.png")
    new Mesh(positions, textureCoordinates, indices, texture)
  }

  // A cube despite the name
  private val ball = {
    val ball = new GameObject(cubeMesh)
    ball.position.z = -2.0f
    ball
  }

  private val gameObjects = {
    val ball2 = new GameObject(cubeMesh)
    ball2.position.z = -1.0f
    ball2.position.x = 0.7f
    ball2.scale = 0.5f
    val ball3 = new GameObject(cubeMesh)
    ball3.position.z = -1.0f
    ball3.position.x = -0.7f
    ball3.scale = 0.5f
    Array(ball, ball2, ball3)
  }

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
      ball.position.z = -2.0f
    }
  }

  def render(window: Window): Unit = {
    renderer.render(window, gameObjects)
  }

  def cleanup(): Unit = {
    renderer.cleanup()
  }
}
