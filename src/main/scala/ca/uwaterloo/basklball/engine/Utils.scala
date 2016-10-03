package ca.uwaterloo.basklball.engine

import scala.io.Source

object Utils {
  def loadResources(resource: String): String = {
    val stream = getClass.getResourceAsStream(resource)
    Source.fromInputStream(stream).mkString
  }

  def readAllLines(resource: String): Array[String] = {
    loadResources(resource).split('\n')
  }
}
