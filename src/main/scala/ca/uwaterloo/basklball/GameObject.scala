package ca.uwaterloo.basklball

import org.joml.Vector3f

class GameObject(val mesh: Mesh) {
  val position = new Vector3f(0, 0, 0)
  val rotation = new Vector3f(0, 0, 0)
  private var _scale = 1.0f

  def scale = _scale
  def scale_=(scale: Float) { _scale = scale }

  def reset(): Unit = {
    position.x = 0.0f
    position.y = 0.0f
    position.z = 0.0f
    rotation.x = 0.0f
    rotation.y = 0.0f
    rotation.z = 0.0f
    scale = 1.0f
  }
}
