package ca.uwaterloo.basklball.game

import org.joml.Vector3f

/**
  * Created by sam on 2016-09-30.
  */
object GameState {
  val BALL_RADIUS = 0.5f
  val FINGER_LENGTH = BALL_RADIUS/2f
  val PALM_LENGTH = BALL_RADIUS/2f
  val FOREARM_LENGTH = BALL_RADIUS
  val UPPERARM_LENGTH = BALL_RADIUS*1.2f
}

class GameState {
  private var _score = 0f
  def score = _score
  // All vectors are y,z,pitch co-ordinates, NOT x,y,z. Vector3f is used for convenience over tuples, which are useless.
  // This game takes place on a 2f plan going away from the camera. Thus, all positions are away from the camera, and in
  // the y,z plane. There is no way to add x (yaw) or y (roll) rotation, only pitch (aka top/backspin on the ball).

  // Up is positive, down is negative. Thus, rotations "to the ground" are negative, and rotations "to the face" are
  // positive.

  // The joints are referenced from their connection to the body. So, fingersPosition == the position of the knuckle.
  // The shoulder joint is the reference point of y = 0, z = 0.
  // Angles for joints are zeroed at the position of reaching as high as possible.
  private var _upperarmPosition = new Vector3f(0f, 0f, -90f)
  def upperarmPosition = _upperarmPosition
  def upperarmNetAngle = -_upperarmPosition.z
  private var _upperarmRotationVelocity = 0f
  private val _maxUpperarmAngle = 0f; // shoulder, when as straight as can be, is as much as it bends in that direction
  private val _minUpperarmAngle = -270f; // shoulder can do about 3/4 of a full rotation

  private var _forearmPosition = new Vector3f(0f, _upperarmPosition.y + GameState.UPPERARM_LENGTH, 135f)
  def forearmPosition = _forearmPosition
  def forearmNetAngle = _forearmPosition.z - upperarmNetAngle
  private var _forearmRotationVelocity = 0f
  private val _maxForearmAngle = 140f; // elbow can bend towards you somewhat close to 180
  private val _minForearmAngle = 0f; // elbow can't bend past straight

  private var _palmPosition =
    new Vector3f(GameState.FOREARM_LENGTH * Math.cos(Math.toRadians(forearmNetAngle)).toFloat + _forearmPosition.x,
                 GameState.FOREARM_LENGTH * Math.sin(Math.toRadians(forearmNetAngle)).toFloat - _forearmPosition.y,
                 45f)
  def palmPosition = _palmPosition
  def palmNetAngle = _palmPosition.z + forearmNetAngle
  private var _palmRotationVelocity = 0f
  private val _maxPalmAngle = 90f; // wrist can bend back 90 deg
  private val _minPalmAngle = -90f; // wrist can bend forward 90 deg

  private var _fingerPosition = new Vector3f(_palmPosition.x, _palmPosition.y + GameState.PALM_LENGTH, -10f)
  def fingerPosition = _fingerPosition
  def fingerNetAngle = _fingerPosition.z + palmNetAngle
  private var _fingerRotationVelocity = 0f
  private val _maxFingerAngle = 80f; // fingers can't quite bend back to 90 deg
  private val _minFingerAngle = -100f; // fingers can only bend forward slightly past 90 relative to the hand


  // The ball is referenced from its center.
  private var _ballPosition = new Vector3f(_fingerPosition.x + GameState.BALL_RADIUS*1.05f,
                                           _fingerPosition.y,
                                           0f)
  def ballPosition = _ballPosition
  private var _ballVelocity = new Vector3f(0f, 0f, 0f)

  // Game should stay frozen until a key is pressed
  private var _anyPressedYet = false

  def distance(p1:(Float, Float), p2:(Float,Float), p0:(Float,Float)): Float = {
    val distanceToLine = Math.abs((p2._1-p1._1)*(p1._2-p0._2)-(p1._1-p0._1)*(p2._2-p1._2))/Math.sqrt(Math.pow(p2._1-p1._1,2)+Math.pow(p2._2-p1._2,2)).toFloat
    val distanceToP1 = Math.sqrt(Math.pow(p1._1-p0._1,2) + Math.pow(p1._2-p0._2,2)).toFloat
    val distanceToP2 = Math.sqrt(Math.pow(p2._1-p0._1,2) + Math.pow(p2._2-p0._2,2)).toFloat
    Math.max(distanceToLine,Math.max(distanceToP1, distanceToP2))
  }
  // Update state of game
  // Needs time passed in s, as well as whether we are sending "power" to each joint
  def update(timePassed: Long, fingerJoint: Boolean, palmJoint: Boolean, forearmJoint: Boolean,
             upperarmJoint: Boolean): Unit = {
    _anyPressedYet = _anyPressedYet || fingerJoint || palmJoint || forearmJoint || upperarmJoint
    if (_anyPressedYet) {
      // Joint rotations
      if (fingerJoint) _fingerRotationVelocity -= 0.005f
      else _fingerRotationVelocity += 0.001f
      if (palmJoint) _palmRotationVelocity -= 0.005f
      else _palmRotationVelocity += 0.001f
      if (forearmJoint) _forearmRotationVelocity -= 0.005f
      else _forearmRotationVelocity += 0.001f
      if (upperarmJoint) _upperarmRotationVelocity += 0.005f
      else _upperarmRotationVelocity -= 0.001f

      // Performing rotations
      _upperarmPosition.z += _upperarmRotationVelocity * timePassed
      if (_upperarmPosition.z > _maxUpperarmAngle) { _upperarmPosition.z = _maxUpperarmAngle; _upperarmRotationVelocity = 0 }
      if (_upperarmPosition.z < _minUpperarmAngle) { _upperarmPosition.z = _minUpperarmAngle; _upperarmRotationVelocity = 0 }
      _forearmPosition.z += _forearmRotationVelocity * timePassed
      if (_forearmPosition.z > _maxForearmAngle) { _forearmPosition.z = _maxForearmAngle; _forearmRotationVelocity = 0 }
      if (_forearmPosition.z < _minForearmAngle) { _forearmPosition.z = _minForearmAngle; _forearmRotationVelocity = 0 }
      _palmPosition.z += _palmRotationVelocity * timePassed
      if (_palmPosition.z > _maxPalmAngle) { _palmPosition.z = _maxPalmAngle; _palmRotationVelocity = 0 }
      if (_palmPosition.z < _minPalmAngle) { _palmPosition.z = _minPalmAngle; _palmRotationVelocity = 0 }
      _fingerPosition.z += _fingerRotationVelocity * timePassed
      if (_fingerPosition.z > _maxFingerAngle) { _fingerPosition.z = _maxFingerAngle; _fingerRotationVelocity = 0 }
      if (_fingerPosition.z < _minFingerAngle) { _fingerPosition.z = _minFingerAngle; _fingerRotationVelocity = 0 }

      // Updating joint positions
      _forearmPosition.x = GameState.UPPERARM_LENGTH * Math.cos(Math.toRadians(upperarmNetAngle)).toFloat
      _forearmPosition.y = GameState.UPPERARM_LENGTH * Math.sin(Math.toRadians(upperarmNetAngle)).toFloat
      _palmPosition.x = GameState.FOREARM_LENGTH * Math.cos(Math.toRadians(forearmNetAngle)).toFloat + _forearmPosition.x
      _palmPosition.y = GameState.FOREARM_LENGTH * Math.sin(Math.toRadians(forearmNetAngle)).toFloat - _forearmPosition.y
      _fingerPosition.x = GameState.PALM_LENGTH * Math.cos(Math.toRadians(palmNetAngle)).toFloat + _palmPosition.x
      _fingerPosition.y = GameState.PALM_LENGTH * Math.sin(Math.toRadians(palmNetAngle)).toFloat + _palmPosition.y
      val fingertipX = GameState.FINGER_LENGTH * Math.cos(Math.toRadians(fingerNetAngle)).toFloat + _fingerPosition.x
      val fingertipY = GameState.FINGER_LENGTH * Math.cos(Math.toRadians(fingerNetAngle)).toFloat + _fingerPosition.y

      var ballAccel = new Vector3f(0f)
      ballAccel.x = -0.00005f

      val distanceFinger = distance((_fingerPosition.x, _fingerPosition.y), (fingertipX, fingertipY), (_ballPosition.x, _ballPosition.y)) - GameState.FINGER_LENGTH*0.3f/2f
      val distancePalm = distance((_fingerPosition.x, _fingerPosition.y), (_palmPosition.x, _palmPosition.y), (_ballPosition.x, _ballPosition.y)) - GameState.PALM_LENGTH*0.3f/2f
      val distanceForearm = distance((_forearmPosition.x, _forearmPosition.y), (_palmPosition.x, _palmPosition.y), (_ballPosition.x, _ballPosition.y)) - GameState.FOREARM_LENGTH*0.3f/2f

      if (distanceFinger < GameState.BALL_RADIUS) {
        ballAccel.x = (GameState.BALL_RADIUS - distanceFinger) * Math.sin(Math.toRadians(fingerNetAngle)).toFloat / timePassed - _ballVelocity.x
        ballAccel.y = -((GameState.BALL_RADIUS - distanceFinger) * Math.cos(Math.toRadians(fingerNetAngle)).toFloat / timePassed)
        if (distancePalm < GameState.BALL_RADIUS || distanceForearm < GameState.BALL_RADIUS) {
          // Moving ball
          _ballVelocity.add(ballAccel)
          _ballPosition.add(new Vector3f(_ballVelocity).mul(timePassed))
        }
      }
      if (distanceForearm < GameState.BALL_RADIUS) {
        ballAccel.x = (GameState.BALL_RADIUS - distanceForearm) * Math.sin(Math.toRadians(forearmNetAngle)).toFloat / timePassed - _ballVelocity.x
        ballAccel.y = -((GameState.BALL_RADIUS - distanceForearm) * Math.cos(Math.toRadians(forearmNetAngle)).toFloat / timePassed)
        if (distancePalm < GameState.BALL_RADIUS) {
          // Moving ball
          _ballVelocity.add(ballAccel)
          _ballPosition.add(new Vector3f(_ballVelocity).mul(timePassed))
        }
      }
      if (distancePalm < GameState.BALL_RADIUS) {
        ballAccel.x = (GameState.BALL_RADIUS-distancePalm)*Math.sin(Math.toRadians(palmNetAngle)).toFloat/timePassed - _ballVelocity.x
        ballAccel.y = -((GameState.BALL_RADIUS-distancePalm)*Math.cos(Math.toRadians(palmNetAngle)).toFloat/timePassed)
      }

      if (_ballPosition.x < GameState.BALL_RADIUS) {
        _ballVelocity = new Vector3f(0f)
      } else {
        // Moving ball
        _score = -_ballPosition.y
        _ballVelocity.add(ballAccel)
        _ballPosition.add(new Vector3f(_ballVelocity).mul(timePassed))
      }
    }
  }
}
