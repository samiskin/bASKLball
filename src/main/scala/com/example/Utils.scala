package com.example

import scala.io.Source

object Utils {
  def loadResources(resource: String): String = {
    val stream = getClass.getResourceAsStream(resource)
    Source.fromInputStream(stream).mkString
  }
}
