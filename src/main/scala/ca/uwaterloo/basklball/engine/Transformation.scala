package ca.uwaterloo.basklball.engine

import org.joml.Matrix4f

object Transformation {
  def toRad(degree: Float) = Math.toRadians(degree.toDouble).toFloat
}

class Transformation {
  import Transformation.toRad

  // Avoid allocating matrices often
  private val projMatrixBuffer = new Matrix4f()
  private val viewMatrixBuffer = new Matrix4f()
  private val modelViewMatrixBuffer = new Matrix4f()

  // Calls to projectionMatrix() mutate the same underlying matrix
  def projectionMatrix(fov: Float,
                       width: Float,
                       height: Float,
                       zNear: Float,
                       zFar: Float) : Matrix4f = {
    val aspectRatio = width / height
    projMatrixBuffer
      .identity()
      .perspective(fov, aspectRatio, zNear, zFar)
  }

  // Calls to viewMatrix() mutate the same underlying matrix
  def viewMatrix(camera: Camera): Matrix4f = {
    val pos = camera.position
    val rot = camera.rotation
    viewMatrixBuffer
      .identity()
      .rotateXYZ(toRad(rot.x), toRad(rot.y), toRad(rot.z))
      .translate(-pos.x, -pos.y, -pos.z)
  }

  // Calls to modelViewMatrix() mutate the same underlying matrix
  def modelViewMatrix(obj: GameObject, viewMatrix: Matrix4f): Matrix4f = {
    modelViewMatrixBuffer
      .identity()
      .translate(obj.position)
      .rotateXYZ(toRad(obj.rotation.x), toRad(obj.rotation.y), toRad(obj.rotation.z))
      .scale(obj.scale)
    viewMatrix.mul(modelViewMatrixBuffer, modelViewMatrixBuffer)
  }
}
