package com.yuracodir.common.resources

import com.badlogic.gdx.Gdx
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
import com.badlogic.gdx.utils.Disposable
import java.util.*

class Resources {
  private val resources = mutableMapOf<String, Any>()
  private val localizationProvider = LocalizationProvider()
  private val emptySprite = Sprite(Texture(Pixmap(0, 0, Pixmap.Format.RGB565)))
  private val emptySpriteArray = listOf(emptySprite)
  private val emptyBitmapFont = BitmapFont()
  private val emptySkin = Skin()
  private val emptyTextureAtlas = TextureAtlas()
  private val emptyColor = Color.valueOf("#00000000")
  private val emptyParticle = ParticleEffect()
  private val emptyNinePatch = NinePatchDrawable()
  private val emptySound = EmptySound()
  private val textureAtlasCallback = TextureAtlasCallback { manager, key ->
    val atlas: TextureAtlas = manager.get(key)
    resources[key] = atlas
    parse(atlas)
  }
  private val bitmapCallback = BitmapFontCallback { manager, key ->
    val font: BitmapFont = manager.get(key)
    resources[key] = font
  }

  private fun particleCallback(value: String) = ParticleCallback(value) { manager, key ->
    val particle: ParticleEffect = manager.get(key)
    resources[key] = particle
  }

  private val skinCallback = SkinCallback { manager, key ->
    val font: Skin = manager.get(key)
    resources[key] = font
  }
  private val soundCallback = SoundCallback { manager, key ->
    val font: Sound = manager.get(key)
    resources[key] = font
  }

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

  fun getString(key: String): String = localizationProvider.get(key) ?: (key)
  fun getString(key: String, vararg values: Any): String = localizationProvider.get(key, *values) ?: (key)

  fun loadWithAssetManager(resClass: Class<*>): AssetManager {
    val assetManager = AssetManager()
    resClass.classes.forEach { cls ->
      cls.fields.forEach { field ->
        if (field.type == String::class.java) {
          val value = field.get(resClass).toString()
          when (cls.simpleName) {
            "atlas" -> assetManager.load(value, TextureAtlas::class.java, textureAtlasCallback)
            "font" -> assetManager.load(value, BitmapFont::class.java, bitmapCallback)
            "particle" -> assetManager.load(value, ParticleEffect::class.java, particleCallback(value))
            "skin" -> assetManager.load(value, Skin::class.java, skinCallback)
            "color" -> addColor(value)
            "locale" -> addLocale(value)
            "sound" -> assetManager.load(value, Sound::class.java, soundCallback)
          }
        }
      }
    }
    localizationProvider.setLocale(Locale.getDefault().language)
    return assetManager
  }

  fun loadAsync(resClass: Class<*>, callback: (Int) -> Unit) {
    val assetManager = loadWithAssetManager(resClass)
    Thread {
      Gdx.app.postRunnable {
        while (!assetManager.update()) {
          callback.invoke((assetManager.progress * 100f).toInt())
        }
      }
    }.start()
  }

  fun loadSync(resClass: Class<*>) {
    val assetManager = loadWithAssetManager(resClass)
    assetManager.finishLoading()
  }

  fun reflectiveLoad(resClass: Class<*>) {
    resClass.classes.forEach { cls ->
      cls.fields.forEach { field ->
        if (field.type == String::class.java) {
          val value = field.get(resClass).toString()
          when (cls.simpleName) {
            "atlas" -> addTextureAttlas(value)
            "font" -> addFont(value)
            "particle" -> addParticle(value)
            "skin" -> addSkin(value)
            "color" -> addColor(value)
            "sound" -> addSound(value)
          }
        }
      }
    }
  }

  private fun addSpriteFrame(key: String, sprite: Sprite) {
    if (resources[key] == null) {
      resources[key] = mutableListOf<Sprite>()
    }
    getResource<MutableList<Sprite>>(key)?.add(sprite)
  }

  private fun addSprite(key: String, sprite: Sprite) {
    resources[key] = sprite
  }

  private fun addNinePatch(key: String, sprite: NinePatchDrawable) {
    resources[key] = sprite
  }

  private fun addTextureAttlas(key: String) {
    resources[key] = parse(TextureAtlas(key))
  }

  private fun addFont(key: String) {
    resources[key] = BitmapFont(Gdx.files.internal(key))
  }

  private fun addSkin(key: String) {
    resources[key] = Skin(Gdx.files.internal(key))
  }

  private fun addParticle(key: String) {
    resources[key] = ParticleEffect().apply {
      load(Gdx.files.internal(key), TextureAtlas(key.replaceAfterLast(".", "atlas")))
    }
  }

  private fun addColor(key: String) {
    resources[key] = Color.valueOf(key)
  }

  private fun addSound(key: String) {
    resources[key] = Gdx.audio.newSound(Gdx.files.internal(key))
  }

  private fun addLocale(key: String) {
    localizationProvider.addLocale(key)
  }

  private inline fun <reified T> getResource(key: String): T? {
    val res = resources[key]
    return if (res is T) {
      res
    } else {
      return null
    }
  }

  private fun parse(texture: TextureAtlas): TextureAtlas {
    texture.regions.forEach {
      when {
        it.findValue("split") != null -> addNinePatch(it.name, NinePatchDrawable(texture.createPatch(it.name)))
        it.index != -1 -> addSpriteFrame(it.name, Sprite(it))
        else -> addSprite(it.name, Sprite(it))
      }
    }
    return texture
  }

  fun dispose() {
    resources.values.forEach {
      if (it is Disposable) {
        it.dispose()
      }
    }
    resources.clear()
  }
}