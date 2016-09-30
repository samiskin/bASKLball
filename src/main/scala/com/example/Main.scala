/*******************************************************************************
 * Copyright 2015 Serf Productions, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

/**
 * Inspired by the source code found at: "http://www.lwjgl.org/guide".
 */

package com.example

import org.lwjgl.glfw.GLFW._

object Main extends App {
  private val WIDTH  = 800
  private val HEIGHT = 600

  private var renderer: Renderer = _
  private var window: Window = _

  def run() {
    try {
      window = new Window("bASKLball", WIDTH, HEIGHT)
      window.init()

      renderer = new Renderer()
      renderer.init()

      loop(window)

    } finally {
      cleanup()
    }
  }

  private def cleanup(): Unit = {
    if (renderer != null) {
      renderer.cleanup()
    }
    if (window != null) {
      window.cleanup()
    }
  }

  private def loop(window: Window) {
    var xOffset = 0.0f
    var yOffset = 0.0f

    while (!window.windowShowClose()) {

      if (window.isKeyPressed(GLFW_KEY_W)) {
        yOffset += 0.01f
      }
      if (window.isKeyPressed(GLFW_KEY_A)) {
        xOffset -= 0.01f
      }
      if (window.isKeyPressed(GLFW_KEY_S)) {
        yOffset -= 0.01f
      }
      if (window.isKeyPressed(GLFW_KEY_D)) {
        xOffset += 0.01f
      }

      renderer.render(window, xOffset, yOffset)

      window.update()
    }
  }

  run()
}
