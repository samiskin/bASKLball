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
  private val upperarm = new GameObject(cubeMesh)
  private val forearm = new GameObject(cubeMesh)
  private val palm = new GameObject(cubeMesh)
  private val finger = new GameObject(cubeMesh)

  private val gameObjects = {
    val skybox = new Skybox("/textures/skybox.png")
    skybox.scale = 20.0f
    Array(ball, upperarm, forearm, palm, finger, skybox)
  }

  private val camera = {
    val camera = new Camera()
    camera.position.z = 5f
    camera
  }

  def update(window: Window, interval: Long): Unit = {
    gameState.update(interval,
                     fingerJoint = window.isKeyPressed(GLFW_KEY_A),
                     palmJoint = window.isKeyPressed(GLFW_KEY_S),
                     forearmJoint = window.isKeyPressed(GLFW_KEY_K),
                     upperarmJoint = window.isKeyPressed(GLFW_KEY_L))
    ball.position.y = gameState.ballPosition.x
    ball.position.z = -gameState.ballPosition.y
    ball.rotation.x = gameState.ballPosition.z
    upperarm.position.y = gameState.upperarmPosition.x
    upperarm.position.z = -gameState.upperarmPosition.y
    upperarm.rotation.x = gameState.upperarmPosition.z
    forearm.position.y = gameState.forearmPosition.x
    forearm.position.z = -gameState.forearmPosition.y
    forearm.rotation.x = gameState.forearmPosition.z
    palm.position.y = gameState.palmPosition.x
    palm.position.z = -gameState.palmPosition.y
    palm.rotation.x = gameState.palmPosition.z
    finger.position.y = gameState.fingerPosition.x
    finger.position.z = -gameState.fingerPosition.y
    finger.rotation.x = gameState.fingerPosition.z

    // Camera movements
    if (window.isKeyPressed(GLFW_KEY_LEFT)) camera.position.x -= 0.01f
    if (window.isKeyPressed(GLFW_KEY_RIGHT)) camera.position.x += 0.01f
    if (window.isKeyPressed(GLFW_KEY_UP)) camera.position.z -= 0.01f
    if (window.isKeyPressed(GLFW_KEY_DOWN)) camera.position.z += 0.01f
    if (window.isKeyPressed(GLFW_KEY_9)) camera.rotation.y -= 1f
    if (window.isKeyPressed(GLFW_KEY_0)) camera.rotation.y += 1f
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
