package ca.uwaterloo.basklball.Engine

import org.joml.Vector3f

class Camera(val position: Vector3f, val rotation: Vector3f) {
  def this() {
    this(new Vector3f, new Vector3f)
  }
}
