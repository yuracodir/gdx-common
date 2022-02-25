package com.yuracodir.common.resources

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.yuracodir.common.resources.loaders.*
import java.util.*

class Resources(
  private val assetManager: AssetManager,
  private val fileLoaders: MutableList<AssetResourceLoader> = mutableListOf(
    TextureAtlasFileLoader(),
    SoundFileLoader(),
    SkinFileLoader(),
    BitmapFontFileLoader(),
    ColorFileLoader(),
    LocaleFileLoader(),
    ParticleFileLoader("atlas/particles.atlas"),
  )
) {
  private val resources = mutableMapOf<String, Any>()

  private val emptySprite = Sprite(Texture(Pixmap(0, 0, Pixmap.Format.RGB565)))
  private val emptySpriteArray = listOf(emptySprite)
  private val emptyBitmapFont = BitmapFont()
  private val emptySkin = Skin()
  private val emptyTextureAtlas = TextureAtlas()
  private val emptyColor = Color.valueOf("#00000000")
  private val emptyParticle = ParticleEffect()
  private val emptyNinePatch = NinePatchDrawable()
  private val emptySound = EmptySound()

  fun getSprite(key: String) = getResource<Sprite>(key) ?: (emptySprite)
  fun getAnimation(key: String) = getResource<List<Sprite>>(key) ?: (emptySpriteArray)
  fun getFont(key: String) = getResource<BitmapFont>(key) ?: (emptyBitmapFont)
  fun getSkin(key: String) = getResource<Skin>(key) ?: (emptySkin)
  fun getAtlas(key: String) = getResource<TextureAtlas>(key) ?: (emptyTextureAtlas)
  fun getColor(key: String) = getResource<Color>(key) ?: (emptyColor)
  fun getParticle(key: String) = getResource<ParticleEffect>(key) ?: (emptyParticle)
  fun getNinePatchDrawable(key: String): Drawable = getResource<NinePatchDrawable>(key) ?: (emptyNinePatch)
  fun getSound(key: String) = getResource<Sound>(key) ?: (emptySound)
  fun getDrawable(key: String): Drawable = SpriteDrawable(Sprite(getSprite(key)))
  fun getString(key: String): String = getLocalization()?.get(key) ?: key
  fun getString(key: String, vararg values: Any) = getLocalization()?.get(key, *values) ?: (key)
  fun getLocalization() = getResource<LocalizationProvider>(LocaleFileLoader.KEY_LOCALIZATION)

  fun load(resClass: Class<*>): AssetManager {
    val loadersMap = fileLoaders.associateBy { it.getType() }
    val resourceProvider = object : ResourceProvider {
      override fun add(key: String, value: Any) {
        addResource(key, value)
      }
    }
    resClass.classes.forEach { cls ->
      cls.fields.forEach { field ->
        if (field.type == String::class.java) {
          val value = field.get(resClass).toString()
          val type = cls.simpleName
          loadersMap[type]?.load(assetManager, resourceProvider, value)
        }
      }
    }
    getLocalization()?.setLocale(Locale.getDefault().language)
    return assetManager
  }

  fun addLoader(loader: AssetResourceLoader) {
    fileLoaders += loader
  }

  private fun addResource(key: String, value: Any) {
    resources[key] = value
  }

  @Suppress("UNCHECKED_CAST")
  fun <T> getRawResource(key: String): T? {
    return resources[key] as? T
  }

  private inline fun <reified T> getResource(key: String): T? {
    val res = resources[key]
    return if (res is T) {
      res
    } else {
      return null
    }
  }

  fun dispose() {
    resources.clear()
    assetManager.dispose()
  }

  interface ResourceProvider {
    fun add(key: String, value: Any)
  }

  abstract class AssetResourceLoader(private val type: String) {
    abstract fun load(assetManager: AssetManager, resources: ResourceProvider, path: String)
    fun getType() = type
  }
}