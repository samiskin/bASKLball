package ca.uwaterloo.basklball

import org.lwjgl.opengl._
import GL11._
import GL15._
import GL20._
import GL30._
import org.lwjgl.BufferUtils

class Mesh(positions: Array[Float], indices: Array[Int]) {
  val vertexCount = indices.length

  // vaoId is the ID of a Vertex Array Object. A VAO is an object containing one or more VBOs

  // vboID is the ID of a Vertex Buffer Object which is just a memory buffer in the graphics card
  // memory which stores vertices

  // idxVboId is a VBO which holds indices into the position array. Each index corresponds to one
  // vertex in the mesh. We have the index VBO to reduce memory since vertices are shared between
  // triangles
  val (vaoId, posVboId, idxVboId) = {
    val verticesBuffer = BufferUtils.createFloatBuffer(positions.length)
    verticesBuffer.put(positions).flip()

    val vaoId = glGenVertexArrays()
    glBindVertexArray(vaoId)

    // Position VBO
    val posVboId = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER, posVboId)
    glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW)
    glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

    // Index VBO
    val idxVboId = glGenBuffers()
    val indicesBuffer = BufferUtils.createIntBuffer(indices.length)
    indicesBuffer.put(indices).flip()
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId)
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW)

    glBindBuffer(GL_ARRAY_BUFFER, 0)
    glBindVertexArray(0)

    (vaoId, posVboId, idxVboId)
  }

  def cleanup(): Unit = {
    glDisableVertexAttribArray(0)

    // Delete the VBO and VAO
    glBindBuffer(GL_ARRAY_BUFFER, 0)
    glDeleteBuffers(posVboId)
    glDeleteBuffers(idxVboId)

    glBindVertexArray(0)
    glDeleteVertexArrays(vaoId)
  }
}
