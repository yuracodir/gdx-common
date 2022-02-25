package com.yuracodir.common.resources.loaders

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.yuracodir.common.resources.Resources

class ColorFileLoader : Resources.AssetResourceLoader("color") {
  override fun load(assetManager: AssetManager, resources: Resources.ResourceProvider, path: String) {
    resources.add(path, Color.valueOf(path))
  }
}