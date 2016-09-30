package ca.uwaterloo.basklball

import org.joml.Vector3f

class GameObject(val mesh: Mesh) {
  val position = new Vector3f(0, 0, 0)
  val rotation = new Vector3f(0, 0, 0)
  private var _scale = 1.0f

  def scale = _scale
  def scale_=(scale: Float) { _scale = scale }
}
