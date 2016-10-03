package ca.uwaterloo.basklball.Game

import ca.uwaterloo.basklball.Engine._
import org.lwjgl.glfw.GLFW._

object Game {
  val WIDTH  = 800
  val HEIGHT = 600
}

class Game {
  private val renderer = new Renderer()
  private var gameState = new GameState()

  private val cubeMesh = OBJLoader.loadMesh("/models/metal-prism.obj")
  private val sphereMesh = OBJLoader.loadMesh("/models/sphere.obj")

  // A cube despite the name
  private val ball = new GameObject(sphereMesh)
  private val upperarm = {
    val upperarm = new GameObject(cubeMesh, scale=GameState.UPPERARM_LENGTH)
    //upperarm.rotation.z = 180
    upperarm
  }
  private val forearm = new GameObject(cubeMesh, scale=GameState.FOREARM_LENGTH)
  private val palm = new GameObject(cubeMesh, scale=GameState.PALM_LENGTH)
  private val finger = new GameObject(cubeMesh, scale=GameState.FINGER_LENGTH)

  private val gameObjects = {
    val skybox = new Skybox("/textures/skybox.png")
    skybox.scale = 20.0f
    //Array(ball, upperarm, forearm, palm, finger, skybox) TODO UNCOMMENT
    Array(upperarm, forearm, palm, finger, skybox)
  }

  private val camera = {
    val camera = new Camera()
    camera.position.z = 3f
    camera
  }

  def update(window: Window, interval: Long): Unit = {
    if (window.isKeyPressed(GLFW_KEY_RIGHT)) camera.position.x += 0.05f
    if (window.isKeyPressed(GLFW_KEY_LEFT)) camera.position.x -= 0.05f
    if (window.isKeyPressed(GLFW_KEY_UP)) camera.position.z -= 0.05f
    if (window.isKeyPressed(GLFW_KEY_DOWN)) camera.position.z += 0.05f
    if (window.isKeyPressed(GLFW_KEY_9)) camera.rotation.y -= 1f
    if (window.isKeyPressed(GLFW_KEY_0)) camera.rotation.y += 1f

    if (window.isKeyPressed(GLFW_KEY_ENTER)) gameState = new GameState()

    gameState.update(interval,
                     fingerJoint = window.isKeyPressed(GLFW_KEY_A),
                     palmJoint = window.isKeyPressed(GLFW_KEY_S),
                     forearmJoint = window.isKeyPressed(GLFW_KEY_K),
                     upperarmJoint = window.isKeyPressed(GLFW_KEY_L))
    ball.position.y = gameState.ballPosition.x
    ball.position.z = gameState.ballPosition.y
    ball.rotation.x = gameState.ballPosition.z

    upperarm.position.y = gameState.upperarmPosition.x
    upperarm.position.z = gameState.upperarmPosition.y
    upperarm.rotation.x = -gameState.upperarmNetAngle

    forearm.position.y = gameState.forearmPosition.x
    forearm.position.z = -gameState.forearmPosition.y
    forearm.rotation.x = gameState.forearmNetAngle

    palm.position.y = gameState.palmPosition.x
    palm.position.z = gameState.palmPosition.y
    palm.rotation.x = gameState.palmNetAngle

    finger.position.y = gameState.fingerPosition.x
    finger.position.z = gameState.fingerPosition.y
    finger.rotation.x = gameState.fingerNetAngle

    /*
    val (position, rotation) = {
        (camera.position, camera.rotation)
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
    if (window.isKeyPressed(GLFW_KEY_SPACE)) {
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
