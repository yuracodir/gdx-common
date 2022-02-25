package com.yuracodir.common.resources.loaders

import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.SoundLoader
import com.badlogic.gdx.audio.Sound
import com.yuracodir.common.resources.Resources

class SoundFileLoader : Resources.AssetResourceLoader("sounds") {
  override fun load(assetManager: AssetManager, resources: Resources.ResourceProvider, path: String) {
    assetManager.load(path, Sound::class.java, SoundLoader.SoundParameter().also {
      it.loadedCallback = AssetLoaderParameters.LoadedCallback { assetManager, fileName, _ ->
        val sound = assetManager.get<Sound>(fileName)
        resources.add(fileName, sound)
      }
    })
  }
}