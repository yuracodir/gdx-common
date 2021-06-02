package com.yuracodir.common.application

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.yuracodir.common.screens.GdxScreen
import com.yuracodir.screens.ContainerScreen
import com.yuracodir.screens.Screen

class ApplicationConfig(
  val width: Float,
  val height: Float,
)

abstract class Application(protected val configuration: ApplicationConfig) : ApplicationAdapter(), ContainerScreen {
  protected val guiStage: Stage by lazy {
    createGuiStage().apply {
      addActor(backgroundGroup)
    }
  }
  protected val inputProcessor = InputMultiplexer()
  protected val backgroundGroup = WidgetGroup().apply {
    setFillParent(true)
    touchable = Touchable.childrenOnly
  }

  protected open fun createGuiStage(): Stage {
    return Stage(ScreenViewport(
      OrthographicCamera(configuration.width, configuration.height)),
      SpriteBatch())
  }

  override fun create() {
    Gdx.input.inputProcessor = inputProcessor
    inputProcessor.addProcessor(guiStage)
  }

  override fun render() {
    guiStage.act()
    guiStage.draw()
  }

  override fun resize(width: Int, height: Int) {
    super.resize(width, height)
    (childRouter.currentScreen as? GdxScreen)?.also {
      it.resize(width, height)
    }
  }

  override fun dispose() {
  }

  override fun attach(screen: Screen<*>) {
    if (screen is GdxScreen) {
      backgroundGroup.addActor(screen.root)
    }
  }

  override fun detach(screen: Screen<*>) {
    if (screen is GdxScreen) {
      screen.root.remove()
    }
  }
}

