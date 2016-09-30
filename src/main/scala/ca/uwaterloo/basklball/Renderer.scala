package ca.uwaterloo.basklball

import org.lwjgl.opengl._
import GL11._

class Renderer {
  private val FOV = Math.toRadians(60.0f).toFloat
  private val Z_NEAR = 0.01f
  private val Z_FAR = 1000.0f

  private val shaderProgram: ShaderProgram = new ShaderProgram
  shaderProgram.createVertexShader(Utils.loadResources("/shaders/vertex.vs.glsl"))
  shaderProgram.createFragmentShader(Utils.loadResources("/shaders/fragment.fs.glsl"))
  shaderProgram.link()
  shaderProgram.createUniform("projectionMatrix")
  shaderProgram.createUniform("worldMatrix")
  shaderProgram.createUniform("texture_sampler")

  private val transformation = new Transformation

  def clear(): Unit = {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
  }

  def render(window: Window, gameObjects: Array[GameObject]): Unit = {
    clear()

    val projectionMatrix = transformation.projectionMatrix(
      FOV, window.width.toFloat, window.height.toFloat, Z_NEAR, Z_FAR)

    shaderProgram.bind()
    shaderProgram.setUniform("projectionMatrix", projectionMatrix)
    shaderProgram.setUniform("texture_sampler", 0)

    for (gameObject <- gameObjects) {
      val worldMatrix =
        transformation.worldMatrix(gameObject.position, gameObject.rotation, gameObject.scale)
      shaderProgram.setUniform("worldMatrix", worldMatrix)

      gameObject.mesh.render()
    }

    shaderProgram.unbind()
  }

  def cleanup(): Unit = {
    shaderProgram.cleanup()
  }
}
