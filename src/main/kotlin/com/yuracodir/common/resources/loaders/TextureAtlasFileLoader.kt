package com.yuracodir.common.resources.loaders

import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.yuracodir.common.resources.Resources

class TextureAtlasFileLoader : Resources.AssetResourceLoader("atlas") {
  override fun load(assetManager: AssetManager, resources: Resources.ResourceProvider, path: String) {
    assetManager.load(path, TextureAtlas::class.java, TextureAtlasLoader.TextureAtlasParameter().also {
      it.loadedCallback = AssetLoaderParameters.LoadedCallback { assetManager, fileName, _ ->
        val atlas = assetManager.get<TextureAtlas>(fileName)
        resources.add(fileName, atlas)
        val animations = mutableMapOf<String, MutableList<Sprite>>()
        atlas.regions.forEach { region ->
          when {
            region.findValue("split") != null ->
              resources.add(region.name, NinePatchDrawable(atlas.createPatch(region.name)))
            region.index != -1 -> {
              animations.getOrPut(region.name) { mutableListOf() }.add(Sprite(region))
            }
            else -> resources.add(region.name, Sprite(region))
          }
        }
        animations.forEach { entry ->
          resources.add(entry.key, entry.value)
        }
      }
    })
  }
}