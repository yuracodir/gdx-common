package com.yuracodir.common.resources

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound

class EmptySound : Sound {
  override fun pause() {}
  override fun pause(soundId: Long) {}
  override fun setPitch(soundId: Long, pitch: Float) {}
  override fun setPan(soundId: Long, pan: Float, volume: Float) {}
  override fun setLooping(soundId: Long, looping: Boolean) {}
  override fun play() = 0L
  override fun play(volume: Float) = 0L
  override fun play(volume: Float, pitch: Float, pan: Float) = 0L
  override fun stop() {}
  override fun stop(soundId: Long) {}
  override fun setVolume(soundId: Long, volume: Float) {}
  override fun resume() {}
  override fun resume(soundId: Long) {}
  override fun loop() = 0L
  override fun loop(volume: Float) = 0L
  override fun loop(volume: Float, pitch: Float, pan: Float) = 0L
  override fun dispose() {}
}

class EmptyMusic : Music {
  override fun isPlaying() = false
  override fun isLooping() = false
  override fun setOnCompletionListener(listener: Music.OnCompletionListener?) {}
  override fun pause() {}
  override fun setPan(pan: Float, volume: Float) {}
  override fun getPosition() = 0f
  override fun setLooping(isLooping: Boolean) {}
  override fun getVolume() = 0f
  override fun play() {}
  override fun stop() {}
  override fun setVolume(volume: Float) {}
  override fun setPosition(position: Float) {}
  override fun dispose() {}
}