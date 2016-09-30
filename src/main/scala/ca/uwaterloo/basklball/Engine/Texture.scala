package ca.uwaterloo.basklball.Engine

import java.nio.ByteBuffer

import de.matthiasmann.twl.utils.PNGDecoder
import de.matthiasmann.twl.utils.PNGDecoder.Format
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL30._

// Image may need to have power of 2 size
object Texture {
  private def loadTexture(resourceName: String): Int = {
    System.out.println(s"Loading texture: $resourceName")
    val decoder = new PNGDecoder(getClass.getResourceAsStream(resourceName))
    val width = decoder.getWidth
    val height = decoder.getHeight
    val buf = ByteBuffer.allocateDirect(4 * width * height)
    decoder.decode(buf, width * 4, Format.RGBA)
    buf.flip()

    System.out.println(s"Texture width: $width")
    System.out.println(s"Texture height: $height")

    val id = glGenTextures()
    glBindTexture(GL_TEXTURE_2D, id)
    glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf)
    glGenerateMipmap(GL_TEXTURE_2D)

    System.out.println("Done loading texture")
    id
  }
}

class Texture(val id: Int) {
  def this(resourceName: String) {
    this(Texture.loadTexture(resourceName))
  }

  def cleanup(): Unit = {
    glDeleteTextures(id)
  }
}
