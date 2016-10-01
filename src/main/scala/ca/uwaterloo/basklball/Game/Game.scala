package ca.uwaterloo.basklball.Game

import ca.uwaterloo.basklball.Engine._
import org.lwjgl.glfw.GLFW._

object Game {
  val WIDTH  = 800
  val HEIGHT = 600
}

class Game {
  private val renderer = new Renderer()
  private val gameState = new GameState()

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
  private val ball = new GameObject(cubeMesh)

  private val gameObjects = {
    val skybox = new Skybox("/textures/skybox.png")
    skybox.scale = 20.0f
    Array(ball, skybox)
  }

  private val camera = {
    val camera = new Camera()
    camera.position.z = 2f
    camera
  }

  def update(window: Window, interval: Float): Unit = {
    gameState.update(interval, false, false, false, false)
    ball.position.y = gameState.ballPosition.x
    ball.position.z = -gameState.ballPosition.y
    ball.rotation.x = gameState.ballPosition.z
    /*
    val (position, rotation) = {
      if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT) || window.isKeyPressed(GLFW_KEY_RIGHT_SHIFT))
        (camera.position, camera.rotation)
      else
        (ball.position, ball.rotation)
    }
    // Position
    if (window.isKeyPressed(GLFW_KEY_W)) {
      position.y += 0.01f
    }
    if (window.isKeyPressed(GLFW_KEY_A)) {
      position.x -= 0.01f
    }
    if (window.isKeyPressed(GLFW_KEY_S)) {
      position.y -= 0.01f
    }
    if (window.isKeyPressed(GLFW_KEY_D)) {
      position.x += 0.01f
    }
    if (window.isKeyPressed(GLFW_KEY_Q)) {
      position.z += 0.01f
    }
    if (window.isKeyPressed(GLFW_KEY_E)) {
      position.z -= 0.01f
    }
    // Rotation
    if (window.isKeyPressed(GLFW_KEY_I)) {
      rotation.x -= 1.0f
    }
    if (window.isKeyPressed(GLFW_KEY_K)) {
      rotation.x += 1.0f
    }
    if (window.isKeyPressed(GLFW_KEY_J)) {
      rotation.y -= 1.0f
    }
    if (window.isKeyPressed(GLFW_KEY_L)) {
      rotation.y += 1.0f
    }
    if (window.isKeyPressed(GLFW_KEY_U)) {
      rotation.z += 1.0f
    }
    if (window.isKeyPressed(GLFW_KEY_O)) {
      rotation.z -= 1.0f
    }
    // Scale
    if (window.isKeyPressed(GLFW_KEY_R)) {
      ball.scale *= 1.01f
    }
    if (window.isKeyPressed(GLFW_KEY_F)) {
      ball.scale *= 0.99f
    }
    if (window.isKeyPressed(GLFW_KEY_SPACE)) {
      ball.position.zero()
      ball.rotation.zero()
      ball.scale = 1.0f
      ball.position.z = -2.0f
      camera.position.zero()
      camera.rotation.zero()
    }
    */
  }

  def render(window: Window): Unit = {
    renderer.render(window, camera, gameObjects)
  }

  def cleanup(): Unit = {
    renderer.cleanup()
  }
}
