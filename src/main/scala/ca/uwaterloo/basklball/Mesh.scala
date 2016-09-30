package ca.uwaterloo.basklball

import org.lwjgl.opengl._
import GL11._
import GL15._
import GL20._
import GL30._
import org.lwjgl.BufferUtils

class Mesh(positions: Array[Float]) {
  val vertexCount = positions.length / 3 // coordinates are 3d

  val (vaoId, vboId) = {
    val verticesBuffer = BufferUtils.createFloatBuffer(positions.length)
    verticesBuffer.put(positions).flip

    val vaoId = glGenVertexArrays()
    glBindVertexArray(vaoId)

    val vboId = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER, vboId)
    glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW)
    glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)
    glBindBuffer(GL_ARRAY_BUFFER, 0)

    (vaoId, vboId)
  }

  def cleanup(): Unit = {
    glDisableVertexAttribArray(0)

    // Delete the VBO and VAO
    glBindBuffer(GL_ARRAY_BUFFER, 0)
    glDeleteBuffers(vboId)

    glBindVertexArray(0)
    glDeleteVertexArrays(vaoId)
  }
}
