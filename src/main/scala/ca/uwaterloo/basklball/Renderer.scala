package ca.uwaterloo.basklball

import org.lwjgl.opengl._, GL11._, GL20._, GL30._

class Renderer {
  private val shaderProgram: ShaderProgram = new ShaderProgram()
  shaderProgram.createVertexShader(Utils.loadResources("/vertex.vs.glsl"))
  shaderProgram.createFragmentShader(Utils.loadResources("/fragment.fs.glsl"))
  shaderProgram.link()

  def clear(): Unit = {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
  }

  def render(window:Window, mesh: Mesh): Unit = {
    clear()

    shaderProgram.bind()

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
