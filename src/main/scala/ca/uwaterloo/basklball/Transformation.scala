package ca.uwaterloo.basklball

import org.joml.{Matrix4f}

class Transformation {
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
    viewMatrixBuffer
      .identity()
      .translate(-pos.x, -pos.y, -pos.z)
  }

  // Calls to modelViewMatrix() mutate the same underlying matrix
  def modelViewMatrix(obj: GameObject, viewMatrix: Matrix4f): Matrix4f = {
    def toRad(degree: Float) = Math.toRadians(degree.toDouble).toFloat
    modelViewMatrixBuffer
      .identity()
      .translate(obj.position)
      .rotateXYZ(toRad(obj.rotation.x), toRad(obj.rotation.y), toRad(obj.rotation.z))
      .scale(obj.scale)
    viewMatrix.mul(modelViewMatrixBuffer, modelViewMatrixBuffer)
  }

}
