package ca.uwaterloo.basklball.engine

import org.joml.{Matrix3f, Vector3f}

class Camera(val position: Vector3f, val rotation: Vector3f) {
  import Transformation.toRad

  private val viewMatrix: Matrix3f = new Matrix3f();

  def this() {
    this(new Vector3f, new Vector3f)
  }

  def move(amt: Vector3f): Unit = {
    viewMatrix.identity().rotateXYZ(toRad(rotation.x), toRad(rotation.y), toRad(rotation.z))
    viewMatrix.invert().transform(amt)
    position.add(amt)
  }
}
