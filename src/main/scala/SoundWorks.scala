import org.lwjgl._
import org.lwjgl.openal._
import org.lwjgl.stb._
import java.io._
import java.nio._
import java.util._

import org.lwjgl.openal.AL10._
import org.lwjgl.openal.ALC10.{ALC_DEFAULT_DEVICE_SPECIFIER, ALC_FREQUENCY, ALC_REFRESH, ALC_SYNC, ALC_TRUE, alcCloseDevice, alcCreateContext, alcDestroyContext, alcGetInteger, alcGetString, alcMakeContextCurrent, alcOpenDevice}
import org.lwjgl.openal.ALC11._
import org.lwjgl.openal.EXTThreadLocalContext._
import org.lwjgl.stb.STBVorbis._
import org.lwjgl.system.MemoryUtil._


object SoundWorks {
  def main(args: Array[String]): Unit = {
    val device = alcOpenDevice(null.asInstanceOf[ByteBuffer])
    if (device == NULL) throw new IllegalStateException("Failed to open the default device.")
    val deviceCaps = ALC.createCapabilities(device)
    val defaultDeviceSpecifier = Objects.requireNonNull(alcGetString(NULL, ALC_DEFAULT_DEVICE_SPECIFIER))
    val context = alcCreateContext(device, null.asInstanceOf[IntBuffer])
    alcSetThreadContext(context)
    AL.createCapabilities(deviceCaps)
    try
      testPlayback()
    finally {
      alcMakeContextCurrent(NULL)
      alcDestroyContext(context)
      alcCloseDevice(device)
    }
  }

  private def testPlayback(): Unit = { // generate buffers and sources
    val buffer = alGenBuffers
    val source = alGenSources
    try {
      val info = STBVorbisInfo.malloc
      try {
        val pcm = readVorbis("src/resources/Sound/REOL - No title.ogg", 32 * 1024, info)
        //copy to buffer
        alBufferData(buffer, if (info.channels == 1) AL_FORMAT_MONO16
        else AL_FORMAT_STEREO16, pcm, info.sample_rate)
      } finally if (info != null) info.close()
    }
    //set up source input
    alSourcei(source, AL_BUFFER, buffer)
    //play source
    alSourcePlay(source)
    //wait
    System.out.println("Waiting for sound to complete...")
    while (true) {Thread.sleep(1000)}
    //stop source 0
    alSourceStop(source)
    //delete buffers and sources
    alDeleteSources(source)
    alDeleteBuffers(buffer)
  }

  def readVorbis(resource: String, bufferSize: Int, info: STBVorbisInfo): ShortBuffer = {
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

final class SoundWorks private() {
}