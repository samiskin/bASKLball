package ca.uwaterloo.basklball.game

import org.joml.Vector3f
import ca.uwaterloo.basklball.engine._
import org.lwjgl.glfw.GLFW._

object Game {
  val WIDTH  = 800
  val HEIGHT = 600
}

class Game {
  private val renderer = new Renderer()
  private var gameState = new GameState()

  private val obeliskMesh = OBJLoader.loadMesh("/models/obelisk-split.obj")
  private val obeliskTexture = new Texture("/textures/obelisk.png")
  obeliskMesh.texture = obeliskTexture
  private val cubeMesh = OBJLoader.loadMesh("/models/metal-obelisk.obj")
  private val metalTexture = new Texture("/textures/metal.png")
  cubeMesh.texture = metalTexture
  private val sphereMesh = OBJLoader.loadMesh("/models/sphere.obj")

  // A cube despite the name
  sphereMesh.colour = new Vector3f(185,91,1)
  private val ball = new GameObject(sphereMesh, scale=GameState.BALL_RADIUS)
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
    val obelisk = new GameObject(obeliskMesh)
    obelisk.position.x = -1f
    skybox.scale = 20.0f
    Array(ball, upperarm, forearm, palm, finger, skybox)
  }

  private val camera = {
    val camera = new Camera()
    camera.position.x = -0.89f
    camera.position.y = 1.31f
    camera.position.z = 3.42f
    camera
  }

  def update(window: Window, interval: Long): Unit = {
    //TODO - show score!!!
    if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
      val position = new Vector3f
      // Position
      if (window.isKeyPressed(GLFW_KEY_A)) position.x -= 0.01f
      if (window.isKeyPressed(GLFW_KEY_D)) position.x += 0.01f
      if (window.isKeyPressed(GLFW_KEY_Q)) position.y += 0.01f
      if (window.isKeyPressed(GLFW_KEY_E)) position.y -= 0.01f
      if (window.isKeyPressed(GLFW_KEY_W)) position.z -= 0.01f
      if (window.isKeyPressed(GLFW_KEY_S)) position.z += 0.01f
      camera.move(position)
      // Rotation
      val rotation = camera.rotation
      if (window.isKeyPressed(GLFW_KEY_I)) rotation.x -= 1.0f
      if (window.isKeyPressed(GLFW_KEY_K)) rotation.x += 1.0f
      if (window.isKeyPressed(GLFW_KEY_J)) rotation.y -= 1.0f
      if (window.isKeyPressed(GLFW_KEY_L)) rotation.y += 1.0f
      if (window.isKeyPressed(GLFW_KEY_U)) rotation.z += 1.0f
      if (window.isKeyPressed(GLFW_KEY_O)) rotation.z -= 1.0f
      if (window.isKeyPressed(GLFW_KEY_SPACE)) { position.zero(); rotation.zero() }
      return
    }

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
  }

  def render(window: Window): Unit = {
    renderer.render(window, camera, gameObjects)
  }

  def cleanup(): Unit = {
    renderer.cleanup()
  }
}
