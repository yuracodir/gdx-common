package com.yuracodir.common.resources.loaders

import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.BitmapFontLoader
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.yuracodir.common.resources.Resources

class BitmapFontFileLoader : Resources.AssetResourceLoader("font") {

  override fun load(assetManager: AssetManager, resources: Resources.ResourceProvider, path: String) {
    assetManager.load(path, BitmapFont::class.java, BitmapFontLoader.BitmapFontParameter().also {
      it.loadedCallback = AssetLoaderParameters.LoadedCallback { assetManager, fileName, _ ->
        val atlas = assetManager.get<Skin>(fileName)
        resources.add(fileName, atlas)
      }
    })
  }
}