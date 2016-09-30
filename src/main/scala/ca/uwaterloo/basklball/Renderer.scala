package ca.uwaterloo.basklball

import org.lwjgl.opengl._
import GL11._
import GL20._
import GL30._
import org.joml.Matrix4f

class Renderer {
  private val FOV = Math.toRadians(60.0f).toFloat
  private val Z_NEAR = 0.01f
  private val Z_FAR = 1000.0f
  private val PROJECTION_MATRIX = {
    val aspectRatio = Game.WIDTH.toFloat / Game.HEIGHT
    new Matrix4f().perspective(FOV, aspectRatio, Z_NEAR, Z_FAR)
  }

  private val shaderProgram: ShaderProgram = new ShaderProgram()
  shaderProgram.createVertexShader(Utils.loadResources("/vertex.vs.glsl"))
  shaderProgram.createFragmentShader(Utils.loadResources("/fragment.fs.glsl"))
  shaderProgram.link()
  shaderProgram.createUniform("projectionMatrix")

  def clear(): Unit = {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
  }

  def render(window: Window, mesh: Mesh): Unit = {
    clear()

    shaderProgram.bind()
    shaderProgram.setUniform("projectionMatrix", PROJECTION_MATRIX)

    // TODO translation

    // positions, colors, indices, etc are included in the VAO
    glBindVertexArray(mesh.vaoId)
    glEnableVertexAttribArray(0) // enable positions
    glEnableVertexAttribArray(1) // enable colors

    glDrawElements(GL_TRIANGLES, mesh.vertexCount, GL_UNSIGNED_INT, 0)

    // Restore state
    glDisableVertexAttribArray(0)
    glDisableVertexAttribArray(1)
    glBindVertexArray(0)

    shaderProgram.unbind()
  }

  def cleanup(): Unit = {
    shaderProgram.cleanup()
  }
}
