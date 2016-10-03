package ca.uwaterloo.basklball.engine

import org.joml.{Matrix4f, Vector3f}
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._

import scala.collection.mutable

class ShaderProgram {

  private val programId: Int = glCreateProgram()
  if (programId == 0) {
    throw new Exception("Could not create Shader")
  }

  private var vertexShaderId: Int = _
  private var fragmentShaderId: Int = _
  private val uniforms = new mutable.HashMap[String, Int]

  // Retrieve the location of a uniform in the shader program
  def createUniform(uniformName: String): Unit = {
    val uniformLocation = glGetUniformLocation(programId, uniformName)
    if (uniformLocation < 0) {
      throw new Exception("Could not find uniform: " + uniformName)
    }
    uniforms.put(uniformName, uniformLocation)
  }

  // Set a uniform in the shader program
  def setUniform(uniformName: String, value: Matrix4f): Unit = {
    val buffer = BufferUtils.createFloatBuffer(16) // since 4x4 matrix
    value.get(buffer)
    glUniformMatrix4fv(uniforms(uniformName), false, buffer)
  }

  def setUniform(uniformName: String, value: Int): Unit = {
    glUniform1i(uniforms(uniformName), value)
  }

  def setUniform(uniformName: String, value: Vector3f): Unit = {
    glUniform3f(uniforms(uniformName), value.x, value.y, value.z)
  }

  def createVertexShader(shaderCode: String): Unit = {
    vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER)
  }

  def createFragmentShader(shaderCode: String): Unit = {
    fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER)
  }

  protected def createShader(shaderCode: String, shaderType: Int): Int = {
    val shaderId = glCreateShader(shaderType)
    if (shaderId == 0) {
      throw new Exception(s"Error creating shader. Code: $shaderId")
    }

    glShaderSource(shaderId, shaderCode)
    glCompileShader(shaderId)

    if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
      val infoLog = glGetShaderInfoLog(shaderId)
      throw new Exception(s"Error compiling shader: $infoLog")
    }

    glAttachShader(programId, shaderId)
    shaderId
  }

  def link(): Unit = {
    glLinkProgram(programId)
    if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE) {
      val infoLog = glGetProgramInfoLog(programId)
      throw new Exception(s"Error linking shader: $infoLog")
    }

    glValidateProgram(programId)
    if (glGetProgrami(programId, GL_VALIDATE_STATUS) == GL_FALSE) {
      val infoLog = glGetProgramInfoLog(programId)
      System.err.println(s"Warning validating shader: $infoLog")
    }
  }

  def bind(): Unit = {
    glUseProgram(programId)
  }

  def unbind(): Unit ={
    glUseProgram(0)
  }

  def cleanup(): Unit = {
    unbind()
    if (programId != 0) {
      if (vertexShaderId != 0) {
        glDetachShader(programId, vertexShaderId)
      }
      if (fragmentShaderId != 0) {
        glDetachShader(programId, fragmentShaderId)
      }
      glDeleteProgram(programId)
    }
  }
}
