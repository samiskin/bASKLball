package ca.uwaterloo.basklball.Engine

import org.joml.{Vector2f, Vector3f}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by samiskin on 9/30/16.
  */

object Face {}

object OBJLoader {
  def loadMesh(fileName: String): Mesh = {
    val lines = Utils.readAllLines(fileName)
    var vertices = new ArrayBuffer[Vector3f]()
    var textures = new ArrayBuffer[Vector2f]()
    var normals = new ArrayBuffer[Vector3f]()
    var faces = new ArrayBuffer[Face]()

    lines.foreach((line) => {
      val tokens = line.split("\\s+")
      tokens(0) match {
        case "v" => {
          vertices += new Vector3f(
            tokens(1).toFloat,
            tokens(2).toFloat,
            tokens(3).toFloat
          )
        }
        case "vt" => {
          textures += new Vector2f(tokens(1).toFloat, tokens(2).toFloat)
        }
        case "vn" => {
          normals += new Vector3f(
            tokens(1).toFloat,
            tokens(2).toFloat,
            tokens(3).toFloat
          )
        }
        case "f" => {
          faces += new Face(tokens(1), tokens(2), tokens(3))
        }
        case _ => {}
      }
    })

    return reorderLists(vertices.toArray, textures.toArray, normals.toArray, faces.toArray)
  }

  def reorderLists(posList: Array[Vector3f], textCoordList: Array[Vector2f], normList: Array[Vector3f], faceList: Array[Face]): Mesh = {
    val textCoordArr = new Array[Vector2f](posList.length)
    val normArr = new Array[Vector3f](posList.length)
    val indices = new Array[Int](faceList.length * 3)
    var i = 0
    faceList.flatMap(f => f.idxGroups).foreach(idxGroup => {
      println(idxGroup.toString());
      if (idxGroup.idxTextCoord >= 0)
        textCoordArr(idxGroup.idxPos) = textCoordList(idxGroup.idxTextCoord)
      if (idxGroup.idxVecNormal >= 0)
        normArr(idxGroup.idxPos) = normList(idxGroup.idxVecNormal)
      indices(i) = idxGroup.idxPos
      i = i + 1;
    })

    new Mesh(
      posList.flatMap(p => Array(p.x, p.y, p.z)),
      textCoordArr.flatMap(p => if (p == null) Array(0f, 0f) else Array(p.x, p.y)),
      normArr.flatMap(p => if (p == null) Array(0f, 0f, 0f) else Array(p.x, p.y, p.z)),
      indices
    )
  }


  class Face(v1: String, v2: String, v3: String) {
    val idxGroups = Array.fill(3){new IdxGroup}
    idxGroups(0) = parseLine(v1)
    idxGroups(1) = parseLine(v2)
    idxGroups(2) = parseLine(v3)
    def getFaceVertexIndices: Array[IdxGroup] = idxGroups
  }


  private def parseLine(line: String): IdxGroup = {
    val idxGroup = new IdxGroup
    val lineTokens = line.split("/")
    val length = lineTokens.length
    idxGroup.idxPos = lineTokens(0).toInt - 1
    if (length > 1) {
      // Text coords are optional
      val textCoord = lineTokens(1)
      idxGroup.idxTextCoord = if (textCoord.length > 0)
        textCoord.toInt - 1
        else IdxGroup.NO_VALUE
      if (length > 2) {
        idxGroup.idxVecNormal = lineTokens(2).toInt - 1
      }
    }
    return idxGroup
  }

  object IdxGroup { val NO_VALUE: Int = -1 }
  class IdxGroup() {
    var idxPos: Int = IdxGroup.NO_VALUE
    var idxTextCoord: Int = IdxGroup.NO_VALUE
    var idxVecNormal: Int = IdxGroup.NO_VALUE

    override def toString() = idxPos + "/" + idxTextCoord + "/" + idxVecNormal
  }

}
