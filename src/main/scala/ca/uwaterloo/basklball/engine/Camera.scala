package ca.uwaterloo.basklball.engine

import org.joml.Vector3f

class Camera(val position: Vector3f, val rotation: Vector3f) {
  def this() {
    this(new Vector3f, new Vector3f)
  }
}
