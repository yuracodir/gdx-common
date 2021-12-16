package com.yuracodir.common.resources

import com.badlogic.gdx.assets.AssetLoaderParameters.LoadedCallback
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.*

class TextureAtlasCallback(onFinish: (AssetManager, String) -> Unit)
  : TextureAtlasLoader.TextureAtlasParameter() {
  init {
    loadedCallback = LoadedCallback { assetManager, fileName, _ ->
      onFinish.invoke(assetManager, fileName)
    }
  }
}

class BitmapFontCallback(onFinish: (AssetManager, String) -> Unit)
  : BitmapFontLoader.BitmapFontParameter() {
  init {
    loadedCallback = LoadedCallback { assetManager, fileName, _ ->
      onFinish.invoke(assetManager, fileName)
    }
  }
}

class ParticleCallback(atlas: String, onFinish: (AssetManager, String) -> Unit) :
  ParticleEffectLoader.ParticleEffectParameter() {
  init {
    atlasFile = atlas
    loadedCallback = LoadedCallback { assetManager, fileName, _ ->
      onFinish.invoke(assetManager, fileName)
    }
  }
}

class SkinCallback(onFinish: (AssetManager, String) -> Unit) : SkinLoader.SkinParameter() {
  init {
    loadedCallback = LoadedCallback { assetManager, fileName, _ ->
      onFinish.invoke(assetManager, fileName)
    }
  }
}

class SoundCallback(onFinish: (AssetManager, String) -> Unit) : SoundLoader.SoundParameter() {
  init {
    loadedCallback = LoadedCallback { assetManager, fileName, _ ->
      onFinish.invoke(assetManager, fileName)
    }
  }
}