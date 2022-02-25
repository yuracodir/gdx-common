package com.yuracodir.common.resources.loaders

import com.badlogic.gdx.assets.AssetManager
import com.yuracodir.common.resources.LocalizationProvider
import com.yuracodir.common.resources.Resources

class LocaleFileLoader : Resources.AssetResourceLoader("locale") {

  private val locale = LocalizationProvider()

  override fun load(assetManager: AssetManager, resources: Resources.ResourceProvider, path: String) {
    locale.addLocale(path)
    resources.add(KEY_LOCALIZATION, locale)
  }

  companion object {
    const val KEY_LOCALIZATION = "string_localization_resource"
  }

}