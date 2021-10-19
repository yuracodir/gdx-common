package com.yuracodir.common.application

import com.badlogic.gdx.Game

abstract class GdxApplication : Game() {

  override fun create() {
    onCreate()
  }

  abstract fun onCreate()

  override fun dispose() {
    super.dispose()
    setScreen(null)
  }
}