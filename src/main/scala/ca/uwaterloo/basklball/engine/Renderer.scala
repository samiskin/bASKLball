package ca.uwaterloo.basklball.engine

import org.lwjgl.opengl.GL11._

class Renderer {
  private val FOV = Math.toRadians(60.0f).toFloat
  private val Z_NEAR = 0.01f
  private val Z_FAR = 1000.0f

  private val shaderProgram: ShaderProgram = new ShaderProgram
  shaderProgram.createVertexShader(Utils.loadResources("/shaders/vertex.vs.glsl"))
  shaderProgram.createFragmentShader(Utils.loadResources("/shaders/fragment.fs.glsl"))
  shaderProgram.link()
  shaderProgram.createUniform("projectionMatrix")
  shaderProgram.createUniform("modelViewMatrix")
  shaderProgram.createUniform("texture_sampler")
  shaderProgram.createUniform("colour")
  shaderProgram.createUniform("useColour")

  private val transformation = new Transformation

  def clear(): Unit = {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
  }

  def render(window: Window, camera: Camera, gameObjects: Array[GameObject]): Unit = {
    clear()

    val projectionMatrix = transformation.projectionMatrix(
      FOV, window.width.toFloat, window.height.toFloat, Z_NEAR, Z_FAR)

    shaderProgram.bind()
    shaderProgram.setUniform("projectionMatrix", projectionMatrix)
    shaderProgram.setUniform("texture_sampler", 0)

    val viewMatrix = transformation.viewMatrix(camera)

    for (gameObject <- gameObjects) {
      val mesh = gameObject.mesh;
      val modelViewMatrix =
        transformation.modelViewMatrix(gameObject, viewMatrix)
      shaderProgram.setUniform("modelViewMatrix", modelViewMatrix)
      shaderProgram.setUniform("colour", mesh.colour)
      shaderProgram.setUniform("useColour", if (mesh.texture != null) 0 else 1)

      mesh.render()
    }

    shaderProgram.unbind()
  }

  def cleanup(): Unit = {
    shaderProgram.cleanup()
  }
}
