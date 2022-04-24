package com.yuracodir.common.resources.loaders

import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.yuracodir.common.resources.Resources

class ParticleFileLoader(private val atlas: String) : Resources.AssetResourceLoader("particle") {
  override fun load(assetManager: AssetManager, resources: Resources.ResourceProvider, path: String) {
    assetManager.load(path, ParticleEffect::class.java, ParticleEffectLoader.ParticleEffectParameter().also {
      it.atlasFile = atlas
      it.loadedCallback = AssetLoaderParameters.LoadedCallback { assetManager, fileName, _ ->
        val atlas = assetManager.get<ParticleEffect>(fileName)
        resources.add(fileName, atlas)
      }
    })
  }
}