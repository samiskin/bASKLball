package ca.uwaterloo.basklball

import org.joml.Vector3f

class Camera(val position: Vector3f) {
  def this() {
    this(new Vector3f())
  }
}
