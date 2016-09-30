package ca.uwaterloo.basklball

object Skybox {
  def makeMesh(path: String): Mesh = {
    val positions = Array(
      // Front
      -1.0f, 1.0f, 1.0f,
      -1.0f, -1.0f, 1.0f,
      1.0f, -1.0f, 1.0f,
      1.0f, 1.0f, 1.0f,

      // Left Face
      -1.0f, 1.0f, 1.0f,
      -1.0f, -1.0f, 1.0f,
      -1.0f, -1.0f, -1.0f,
      -1.0f, 1.0f, -1.0f,

      // Right Face
      1.0f, 1.0f, 1.0f,
      1.0f, -1.0f, 1.0f,
      1.0f, -1.0f, -1.0f,
      1.0f, 1.0f, -1.0f,

      // Back Face
      -1.0f, 1.0f, -1.0f,
      -1.0f, -1.0f, -1.0f,
      1.0f, -1.0f, -1.0f,
      1.0f, 1.0f, -1.0f,

      // Top Face
      -1.0f, 1.0f, -1.0f,
      -1.0f, 1.0f, 1.0f,
      1.0f, 1.0f, 1.0f,
      1.0f, 1.0f, -1.0f,

      // Bottom Face
      -1.0f, -1.0f, -1.0f,
      -1.0f, -1.0f, 1.0f,
      1.0f, -1.0f, 1.0f,
      1.0f, -1.0f, -1.0f
    )

    val textureCoordinates = Array(
      // Front Face
      0.333333f, 0.5f,
      0.333333f, 1.0f,
      0.0f, 1.0f,
      0.0f, 0.5f,

      // Left Face
      0.0f, 0.0f,
      0.0f, 0.5f,
      0.333333f, 0.5f,
      0.333333f, 0.0f,

      // Right Face
      1.0f, 0.0f,
      1.0f, 0.5f,
      0.666666f, 0.5f,
      0.666666f, 0.0f,

      // Back Face
      0.340000f, 0.0f,
      0.340000f, 0.5f,
      0.666666f, 0.5f,
      0.666666f, 0.0f,

      // Top Face
      0.333333f, 0.5f,
      0.666666f, 0.5f,
      0.666666f, 1.0f,
      0.333333f, 1.0f,


/*      0.666666f, 0.5f,
      0.666666f, 1.0f,
      0.333333f, 1.0f,
      0.333333f, 0.5f,*/

      // Bottom Face
      1.0f, 1.0f,
      1.0f, 0.5f,
      0.666666f, 0.5f,
      0.666666f, 1.0f
    )

    val indices = Array(
      // Front
      0, 1, 2,
      3, 0, 2,
      // Left
      4, 5, 6,
      7, 4, 6,
      // Right
      10, 9, 8,
      10, 8, 11,
      // Back
      12, 13, 14,
      15, 12, 14,
      // Top
      18, 17, 16,
      18, 16, 19,
      // Bottom
      20, 21, 22,
      23, 20, 22
    )

    val texture = new Texture(path)

    new Mesh(positions, textureCoordinates, indices, texture)
  }
}

class Skybox(path: String) extends GameObject(mesh = Skybox.makeMesh(path)) {
}
