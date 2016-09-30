package ca.uwaterloo.basklball

/**
  * Created by sam on 2016-09-30.
  */
class GameState {
  // All vectors are y,z,rotationZ co-ordinates. This game takes place on a 2d plan going away from the camera. Thus,
  // all positions are away from the camera, and in the y,z plane. There is no way to add x or y rotation, only
  // top/backspin (on the ball, but similarly for the arm).

  // Up is positive, down is negative. Thus, rotations "to the ground" are negative, and rotations "to the face" are
  // positive.

  // The ball is referenced from its center.
  private var ballPosition = (Float, Float, Float)
  private var ballVelocity = (Float, Float, Float)
  // The joints are referenced from their connection to the body. So, fingersPosition == the position of the knuckle
  // Angles for joints are zeroed at the position of reaching as high as possible.
  private var fingersPosition = (Float, Float, Float)
  private var fingersVelocity = (Float, Float, Float)
  private var maxFingerAngle = 80f; // fingers can't quite bend back to 90 deg
  private var minFingerAngle = -100f; // fingers can only bend forward slightly past 90 relative to the hand
  private var palmPosition = (Float, Float, Float)
  private var palmVelocity = (Float, Float, Float)
  private var maxPalmAngle = 90f; // wrist can bend back 90 deg
  private var minPalmAngle = -90f; // wrist can bend forward 90 deg
  private var forearmPosition = (Float, Float, Float)
  private var forearmVelocity = (Float, Float, Float)
  private var maxForearmAngle = 140f; // elbow can bend towards you somewhat close to 180
  private var minForearmAngle = 0f; // elbow can't bend past straight
  private var upperarmPosition = (Float, Float, Float)
  private var upperarmVelocity = (Float, Float, Float)
  private var maxUpperarmAngle = 0f; // shoulder, when as straight as can be, is as much as it bends in that direction
  private var minUpperarmAngle = -270f; // shoulder can do about 3/4 of a full rotation
}
