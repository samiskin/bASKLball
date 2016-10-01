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
    val texture = new Texture("/textures/metal.png")
    val mesh = OBJLoader.loadMesh("/models/blender-cube.obj")
    mesh.texture = texture;
    mesh
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
