package com.yuracodir.common.resources.loaders

import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.SkinLoader
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.yuracodir.common.resources.Resources

class SkinFileLoader : Resources.AssetResourceLoader("skin") {
  override fun load(assetManager: AssetManager, resources: Resources.ResourceProvider, path: String) {
    assetManager.load(path, Skin::class.java, SkinLoader.SkinParameter().also {
      it.loadedCallback = AssetLoaderParameters.LoadedCallback { assetManager, fileName, _ ->
        val atlas = assetManager.get<Skin>(fileName)
        resources.add(fileName, atlas)
      }
    })
  }
}