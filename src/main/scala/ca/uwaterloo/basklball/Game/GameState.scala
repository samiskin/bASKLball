package ca.uwaterloo.basklball.Game

/**
  * Created by sam on 2016-09-30.
  */
object GameState {
  val BALL_RADIUS = 1f
  val FINGER_LENGTH = BALL_RADIUS/2
  val PALM_LENGTH = BALL_RADIUS/2
  val FOREARM_LENGTH = BALL_RADIUS
  val UPPERARM_LENGTH = BALL_RADIUS*1.2
}

class GameState {
  // All vectors are y,z,pitch co-ordinates. This game takes place on a 2d plan going away from the camera. Thus,
  // all positions are away from the camera, and in the y,z plane. There is no way to add x (yaw) or y (roll) rotation,
  // only pitch (aka top/backspin on the ball).

  // Up is positive, down is negative. Thus, rotations "to the ground" are negative, and rotations "to the face" are
  // positive.

  // The joints are referenced from their connection to the body. So, fingersPosition == the position of the knuckle
  // Angles for joints are zeroed at the position of reaching as high as possible.
  private var _upperarmPosition = (0f, 0f, 0f)
  def upperarmPosition = _upperarmPosition
  private var _upperarmVelocity = (0f, 0f, 0f)
  private val _maxUpperarmAngle = 0f; // shoulder, when as straight as can be, is as much as it bends in that direction
  private val _minUpperarmAngle = -270f; // shoulder can do about 3/4 of a full rotation

  private var _forearmPosition = (0f, _upperarmPosition._2 + GameState.UPPERARM_LENGTH, 135f)
  def forearmPosition = _forearmPosition
  private var _forearmVelocity = (0f, 0f, 0f)
  private val _maxForearmAngle = 140f; // elbow can bend towards you somewhat close to 180
  private val _minForearmAngle = 0f; // elbow can't bend past straight

  private var _palmPosition = (_forearmPosition._1 + GameState.FOREARM_LENGTH * Math.sin(_forearmPosition._3),
                               _forearmPosition._2 + GameState.FOREARM_LENGTH * Math.cos(_forearmPosition._3),
                               45f)
  def palmPosition = _palmPosition
  private var _palmVelocity = (0f, 0f, 0f)
  private val _maxPalmAngle = 90f; // wrist can bend back 90 deg
  private val _minPalmAngle = -90f; // wrist can bend forward 90 deg

  private var _fingerPosition = (_palmPosition._1, _palmPosition._2 - GameState.PALM_LENGTH, 30f)
  def fingerPosition = _fingerPosition
  private var _fingerVelocity = (0f, 0f, 0f)
  private val _maxFingerAngle = 80f; // fingers can't quite bend back to 90 deg
  private val _minFingerAngle = -100f; // fingers can only bend forward slightly past 90 relative to the hand


  // The ball is referenced from its center.
  private var _ballPosition = (_fingerPosition._1 + GameState.BALL_RADIUS,
                               (_fingerPosition._2 + _palmPosition._2)/2f,
                               0f)
  def ballPosition = _ballPosition
  private var _ballVelocity = (0f, 0f, 0f)
}
