package com.yuracodir.common.application

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.yuracodir.common.resources.Resources
import com.yuracodir.common.screens.GdxScreen
import com.yuracodir.screens.ContainerScreen

abstract class GdxActivity(viewportWidth: Float, viewportHeight: Float) :
  Context(Resources(), Display(Gdx.graphics.width, Gdx.graphics.height, viewportWidth, viewportHeight)),
  ContainerScreen,
  Screen {

  val spriteBatch = SpriteBatch()
  val inputProcessor = InputMultiplexer()

  private val backgroundGroup = WidgetGroup().apply {
    setFillParent(true)
    touchable = Touchable.childrenOnly
  }

  private val foregroundGroup = WidgetGroup().apply {
    setFillParent(true)
    touchable = Touchable.childrenOnly
  }

  val guiStage: Stage = Stage(
    ScreenViewport(OrthographicCamera(display.viewportWidth, display.viewportHeight)),
    spriteBatch)
    .apply {
      addActor(backgroundGroup)
      addActor(foregroundGroup)
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
      backgroundGroup.addActor(screen.root)
    }
  }

  override fun detach(screen: com.yuracodir.screens.Screen<*>) {
    if (screen is GdxScreen) {
      screen.root.remove()
    }
  }
}