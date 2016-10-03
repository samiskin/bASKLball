package ca.uwaterloo.basklball.engine

import java.io.{BufferedReader, InputStreamReader}

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

object Utils {
  def loadResources(resource: String): String = {
    val stream = getClass.getResourceAsStream(resource)
    Source.fromInputStream(stream).mkString
  }

  def readAllLines(resource: String): Array[String] = {
    return loadResources(resource).split('\n')
  }
}
