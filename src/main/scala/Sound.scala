import org.lwjgl._
import org.lwjgl.stb._
import java.io._
import java.nio._

import org.lwjgl.openal.AL10._
import org.lwjgl.stb.STBVorbis._
import org.lwjgl.system.MemoryUtil._


case class Sound(sourceId: Int, bufferId: Int) {
  def play(): Unit = {
    alSourcePlay(sourceId)
  }
  def pause(): Unit = {
    alSourcePause(sourceId)
  }
  def stop(): Unit = {
    alSourceStop(sourceId)
  }
  def delete(): Unit = {
    alDeleteSources(sourceId)
    alDeleteBuffers(bufferId)
  }

  def isPlaying: Boolean = {
    alGetSourcei(sourceId, AL_SOURCE_STATE) == AL_PLAYING
  }

}

object Sound {

  def load(path: String): Sound = {
    val buffer = alGenBuffers
    val source = alGenSources
    println(buffer + " " + source)
    try {
      val info = STBVorbisInfo.malloc
      try {
        val pcm = readVorbis(path, 32 * 1024, info)
        //copy to buffer
        alBufferData(buffer, if (info.channels == 1) AL_FORMAT_MONO16
        else AL_FORMAT_STEREO16, pcm, info.sample_rate)
      } finally if (info != null) info.close()
    }
    //set up source input
    alSourcei(source, AL_BUFFER, buffer)
    Sound(source, buffer)
  }

  private def readVorbis(resource: String, bufferSize: Int, info: STBVorbisInfo): ShortBuffer = {
    var vorbis: ByteBuffer = null
    try
      vorbis = Util.IOUtil.ioResourceToByteBuffer(resource, bufferSize)
    catch {
      case e: IOException =>
        throw new RuntimeException(e)
    }
    val error = BufferUtils.createIntBuffer(1)
    val decoder = stb_vorbis_open_memory(vorbis, error, null)
    if (decoder == NULL) throw new RuntimeException("Failed to open Ogg Vorbis file. Error: " + error.get(0))
    stb_vorbis_get_info(decoder, info)
    val channels = info.channels
    val pcm = BufferUtils.createShortBuffer(stb_vorbis_stream_length_in_samples(decoder) * channels)
    stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm)
    stb_vorbis_close(decoder)
    pcm
  }

}