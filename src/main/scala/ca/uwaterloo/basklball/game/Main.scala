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

package ca.uwaterloo.basklball.game

import ca.uwaterloo.basklball.engine._

object Main extends App {
  private var window: Window = _
  private var game: Game = _

  def run() {
    try {
      window = new Window("bASKLball", Game.WIDTH, Game.HEIGHT)
      game = new Game()

      loop(window)

    } finally {
      cleanup()
    }
  }

  private def cleanup(): Unit = {
    if (window != null) {
      window.cleanup()
    }
    if (game != null) {
      game.cleanup()
    }
  }

  private def loop(window: Window) {
    var prevTime = java.lang.System.currentTimeMillis()
    while (!window.shouldClose) {
      val currTime = java.lang.System.currentTimeMillis()
      game.update(window, currTime - prevTime) // TODO change interval
      game.render(window)

      window.update()
      prevTime = currTime
    }
  }

  run()
}
