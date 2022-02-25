package com.yuracodir.common.application

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.yuracodir.common.screens.GdxScreen
import com.yuracodir.screens.ContainerScreen

abstract class GdxActivity(context: Context, display: Display = context.display) :
  Context(context.resources, display),
  ContainerScreen,
  Screen {

  open val spriteBatch: Batch = SpriteBatch()
  val inputProcessor = InputMultiplexer()

  private val screenContainer = WidgetGroup().apply {
    setFillParent(true)
    touchable = Touchable.childrenOnly
  }

  val guiStage: Stage by lazy {
    Stage(
      ScreenViewport(OrthographicCamera(display.viewportWidth, display.viewportHeight)),
      spriteBatch)
      .apply {
        addActor(screenContainer)
      }
  }

  override fun pause() {
    childRouter.navigator.pause()
  }

  override fun resume() {
    childRouter.navigator.resume()
  }

  override fun hide() {
    dispose()
  }

  override fun show() {
    Gdx.input.inputProcessor = inputProcessor
    inputProcessor.addProcessor(guiStage)
  }

  override fun render(delta: Float) {
    guiStage.act()
    guiStage.draw()
  }

  override fun resize(width: Int, height: Int) {
    display.setSize(width, height)
    (childRouter.currentScreen as? GdxScreen)?.also {
      it.resize(width, height)
    }
  }

  override fun attach(screen: com.yuracodir.screens.Screen<*>) {
    if (screen is GdxScreen) {
      screenContainer.addActor(screen.root)
    }
  }

  override fun detach(screen: com.yuracodir.screens.Screen<*>) {
    if (screen is GdxScreen) {
      screen.root.remove()
    }
  }
}