package ca.uwaterloo.basklball

import org.joml.{Matrix4f, Vector3f}

class Transformation {
  // Avoid allocating matrices often
  private val projMatrixBuffer = new Matrix4f()
  private val worldMatrixBuffer = new Matrix4f()

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

  // Calls to worldMatrix() mutate the same underlying matrix
  def worldMatrix(offset: Vector3f, rotation: Vector3f, scale: Float): Matrix4f = {
    def toRad(degree: Float) = Math.toRadians(degree.toDouble).toFloat
    worldMatrixBuffer
      .identity()
      .translate(offset)
      .rotateXYZ(toRad(rotation.x), toRad(rotation.y), toRad(rotation.z))
      .scale(scale)
  }

}
