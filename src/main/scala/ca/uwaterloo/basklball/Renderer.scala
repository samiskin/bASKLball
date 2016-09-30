package ca.uwaterloo.basklball

import org.lwjgl.BufferUtils
import org.lwjgl.opengl._, GL11._, GL15._, GL20._, GL30._

class Renderer {
  private var shaderProgram: ShaderProgram = _
  private var vaoId: Int = _  // vertex array object
  private var vboId: Int = _  // vertex buffer object

  def init(): Unit = {
    // Vertices of a square
    val vertices = Array(
       0.0f,  0.5f,  0.0f,
      -0.5f, -0.5f,  0.0f,
       0.5f, -0.5f,  0.0f
    )
    val verticesBuffer = BufferUtils.createFloatBuffer(vertices.length)
    verticesBuffer.put(vertices).flip()

    // Create and bind to VAO
    vaoId = glGenVertexArrays()
    glBindVertexArray(vaoId)

    // Create and bind to VBO
    vboId = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER, vboId)
    glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW)

    // Define structure of the data
    glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

    shaderProgram = new ShaderProgram()
    shaderProgram.createVertexShader(Utils.loadResources("/vertex.vs.glsl"))
    shaderProgram.createFragmentShader(Utils.loadResources("/fragment.fs.glsl"))
    shaderProgram.link()

    // Unbind the VBO and VAO
    glBindBuffer(GL_ARRAY_BUFFER, 0)
    glBindVertexArray(0)
  }

  def clear(): Unit = {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
  }

  def render(window: Window, xOffset: Float, yOffset: Float): Unit = {
    clear()
    shaderProgram.bind()

    // TODO translation

    // Bind to the VAO
    glBindVertexArray(vaoId)
    glEnableVertexAttribArray(0)

    // Draw the vertices
    glDrawArrays(GL_TRIANGLES, 0, 3)

    // Restore state
    glDisableVertexAttribArray(0)
    glBindVertexArray(0)

    shaderProgram.unbind()
  }

  def cleanup(): Unit = {
    if (shaderProgram != null) {
      shaderProgram.cleanup()
    }

    glDisableVertexAttribArray(0)

    // Delete the VBO and VAO
    glBindBuffer(GL_ARRAY_BUFFER, 0)
    glDeleteBuffers(vboId)

    glBindVertexArray(0)
    glDeleteVertexArrays(vaoId)
  }
}
