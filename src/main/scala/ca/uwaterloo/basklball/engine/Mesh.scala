package ca.uwaterloo.basklball.engine

import org.joml.Vector3f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._

import scala.collection.mutable.ArrayBuffer

object Mesh {
  var DEFAULT_COLOUR = new Vector3f(1.0f, 1.0f, 1.0f)
}

class Mesh(var positions: Array[Float],
           var textureCoordinates: Array[Float],
           var normals: Array[Float],
           var indices: Array[Int],
           var colour: Vector3f = Mesh.DEFAULT_COLOUR,
           var texture: Texture = null
           ) {


  val vertexCount = indices.length

  // vaoId is the ID of a Vertex Array Object. A VAO is an object containing one or more VBOs

  // vboID is the ID of a Vertex Buffer Object which is just a memory buffer in the graphics card
  // memory which stores vertices

  // idxVboId is a VBO which holds indices into the position array. Each index corresponds to one
  // vertex in the mesh. We have the index VBO to reduce memory since vertices are shared between
  // triangles
  val (vaoId, vboIds) = {
    val vaoId = glGenVertexArrays()
    val vboIds = new ArrayBuffer[Int]
    glBindVertexArray(vaoId)

    // Position VBO
    val posVboId = glGenBuffers()
    val posBuffer = BufferUtils.createFloatBuffer(positions.length)
    posBuffer.put(positions).flip()
    glBindBuffer(GL_ARRAY_BUFFER, posVboId) // use the pos VBO for the glBufferData()
    glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW) // create and initialize buffer data
    glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0) // associate the pos VBO with the VAO
    vboIds += posVboId

    // Texture Coordinate VBO
    val textureVboId = glGenBuffers()
    val textureBuffer = BufferUtils.createFloatBuffer(textureCoordinates.length)
    textureBuffer.put(textureCoordinates).flip()
    glBindBuffer(GL_ARRAY_BUFFER, textureVboId)
    glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW)
    glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0)
    vboIds += textureVboId

    // Index VBO
    val idxVboId = glGenBuffers()
    val indicesBuffer = BufferUtils.createIntBuffer(indices.length)
    indicesBuffer.put(indices).flip()
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId)
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW)
    vboIds += idxVboId

    // Vertex normals VBO
    val vecNormVboId = glGenBuffers()
    val vecNormalsBuffer = BufferUtils.createFloatBuffer(normals.length)
    vecNormalsBuffer.put(normals).flip()
    glBindBuffer(GL_ARRAY_BUFFER, vecNormVboId)
    glBufferData(GL_ARRAY_BUFFER, vecNormalsBuffer, GL_STATIC_DRAW)
    glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
    vboIds += vecNormVboId

    glBindBuffer(GL_ARRAY_BUFFER, 0)
    glBindVertexArray(0)

    (vaoId, vboIds)
  }

  def render(): Unit = {
    // Activate first texture bank
    if (texture != null) {
      glActiveTexture(GL_TEXTURE0)
      glBindTexture(GL_TEXTURE_2D, texture.id)
    }

    // positions, colors, indices, etc are included in the VAO
    glBindVertexArray(vaoId)
    glEnableVertexAttribArray(0) // enable positions
    glEnableVertexAttribArray(1) // enable colors
    glEnableVertexAttribArray(2) // enable norms

    glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0)

    // Restore state
    glDisableVertexAttribArray(0)
    glDisableVertexAttribArray(1)
    glDisableVertexAttribArray(2)
    glBindVertexArray(0)
    glBindTexture(GL_TEXTURE_2D, 0)
  }

  def cleanup(): Unit = {
    glDisableVertexAttribArray(0)

    // Delete the VBO and VAO
    glBindBuffer(GL_ARRAY_BUFFER, 0)
    for (vboId <- vboIds) {
      glDeleteBuffers(vboId)
    }

    if (texture != null)
      texture.cleanup()

    glBindVertexArray(0)
    glDeleteVertexArrays(vaoId)
  }
}
