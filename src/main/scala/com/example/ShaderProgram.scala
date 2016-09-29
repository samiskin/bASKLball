package com.example

import org.lwjgl.opengl.GL20._

class ShaderProgram {

  private val programId: Int = glCreateProgram()
  private var vertexShaderId: Int = _
  private var fragmentShaderId: Int = _

  if (programId == 0) {
    throw new Exception("Could not create Shader")
  }

  @throws(classOf[Exception])
  def createVertexShader(shaderCode: String): Unit = {
    vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER)
  }

  def createFragmentShader(shaderCode: String): Unit = {
    fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER)
  }

  @throws(classOf[Exception])
  protected def createShader(shaderCode: String, shaderType: Int): Int = {
    val shaderId = glCreateShader(shaderType)
    if (shaderId == 0) {
      throw new Exception(s"Error creating shader. Code: $shaderId")
    }

    glShaderSource(shaderId, shaderCode)
    glCompileShader(shaderId)

    if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
      val infoLog = glGetShaderInfoLog(shaderId)
      throw new Exception(s"Error compiling shader: $infoLog")
    }

    glAttachShader(programId, shaderId)
    shaderId
  }

  @throws(classOf[Exception])
  def link(): Unit = {
    glLinkProgram(programId)
    if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
      val infoLog = glGetProgramInfoLog(programId)
      throw new Exception(s"Error linking shader: $infoLog")
    }

    glValidateProgram(programId)
    if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
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
