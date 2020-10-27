package com.yuracodir.common.resources

import com.badlogic.gdx.Gdx
import java.util.*

class LocalizationProvider {
  private val locales = mutableMapOf<String, MutableMap<String, String>>()
  private var defaultLocale = Locale.ENGLISH.language
  private var locale = defaultLocale
  private val delimiterRegex = ":".toRegex()

  fun setLocale(locale: String) {
    if (locales.containsKey(locale)) {
      this.locale = locale
    }
  }

  fun addLocale(file: String) {
    Gdx.files.internal(file)?.apply {
      val locale = nameWithoutExtension().substringAfter("-", "en")
      readString(Charsets.UTF_8.name()).split("\n").forEach {
        val split = it.split(delimiterRegex, 2)
        if (split.size == 2) {
          val (key, value) = split
          locales.getOrPut(locale) { mutableMapOf() }[key.trim()] = value.trim()
        }
      }
    }
  }

  fun get(key: String) = locales[locale]?.get(key) ?: locales[defaultLocale]?.get(key)

  fun get(key: String, vararg values: Any) = get(key)?.let {
    try {
      String.format(it, *values)
    } catch (e: IllegalFormatException) {
      null
    }
  }
}