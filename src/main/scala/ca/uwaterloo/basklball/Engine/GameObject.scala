package ca.uwaterloo.basklball.Engine

import org.joml.Vector3f

class GameObject(val mesh: Mesh,
                 var scale: Float = 1.0f,
                 var position: Vector3f = new Vector3f(0, 0, 0),
                 var rotation: Vector3f = new Vector3f(0, 0, 0)
                ) {
}
