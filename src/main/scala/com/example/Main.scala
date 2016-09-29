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

import org.lwjgl._, glfw._, opengl._
import Callbacks._, GLFW._, GL11._

import org.lwjgl.system.MemoryUtil._

object Main extends App {
  import CallbackHelpers._

  private val WIDTH  = 800
  private val HEIGHT = 600

  private var renderer: Renderer = _

  def run() {
    try {
      GLFWErrorCallback.createPrint(System.err).set()

      val window = init()
      renderer = new Renderer()
      renderer.init()

      loop(window)

      glfwFreeCallbacks(window)
      glfwDestroyWindow(window)
    } finally {
      cleanup()
    }
  }

  @throws(classOf[Exception])
  private def init(): Long = {
    if (!glfwInit())
      throw new IllegalStateException("Unable to initialize GLFW")

    glfwDefaultWindowHints()
    glfwWindowHint(GLFW_VISIBLE,   GLFW_FALSE) // hiding the window
    glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE) // window resizing not allowed


    // Enable higher OpenGL version
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE)

    val window = glfwCreateWindow(WIDTH, HEIGHT, "LWJGL in Scala", NULL, NULL)
    if (window == NULL)
      throw new RuntimeException("Failed to create the GLFW window")

    glfwSetKeyCallback(window, keyHandler _)

    val vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())

    glfwSetWindowPos (
      window,
      (vidMode. width() -  WIDTH) / 2,
      (vidMode.height() - HEIGHT) / 2
    )

    glfwMakeContextCurrent(window)
    glfwSwapInterval(1)
    glfwShowWindow(window)

    GL.createCapabilities()
    glClearColor(0f, 0f, 0f, 0f)

    window
  }

  private def cleanup(): Unit = {
    glfwTerminate() // destroys all remaining windows, cursors, etc...
    glfwSetErrorCallback(null).free()
    if (renderer != null) {
      renderer.cleanup()
    }
  }

  private def loop(window: Long) {
    val offsets = Array(0f,0f)

    while (!glfwWindowShouldClose(window)) {

      if (glfwGetKey(window, GLFW_KEY_W) == GL_TRUE) {
        offsets(1) += 0.01f
      }
      if (glfwGetKey(window, GLFW_KEY_A) == GL_TRUE) {
        offsets(0) -= 0.01f
      }
      if (glfwGetKey(window, GLFW_KEY_S) == GL_TRUE) {
        offsets(1) -= 0.01f
      }
      if (glfwGetKey(window, GLFW_KEY_D) == GL_TRUE) {
        offsets(0) += 0.01f
      }

/*      glBegin(GL_QUADS)
        glColor4f(1, 0, 0, 0)
        glVertex2f(-0.5f + offsets(0), 0.5f + offsets(1))

        glColor4f(0, 1, 0, 0)
        glVertex2f(0.5f + offsets(0), 0.5f + offsets(1))

        glColor4f(0, 0, 1, 0)
        glVertex2f(0.5f + offsets(0), -0.5f + offsets(1))


        glColor4f(1, 1, 1, 0)
        glVertex2f(-0.5f + offsets(0), -0.5f + offsets(1))
      glEnd()*/
      renderer.render(window)

      glfwSwapBuffers(window)
      glfwPollEvents()
    }
  }

  private def keyHandler (
    window: Long, key: Int, scanCode: Int, action: Int, mods: Int
  ): Unit = {
    if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
      glfwSetWindowShouldClose(window, true)
  }

  run()
}
