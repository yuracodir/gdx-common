package com.yuracodir.common.screens

import com.badlogic.gdx.scenes.scene2d.Group
import com.yuracodir.screens.CallbackScreen
import com.yuracodir.screens.ScreenRouter

abstract class GdxScreen(override var router: ScreenRouter) : CallbackScreen<ScreenRouter>() {

  abstract val root: Group

  override fun getName() : String = javaClass.simpleName

  override fun create() {
  }

  override fun destroy() {
  }

  override fun onBack(): Boolean {
    return router.back()
  }

  override fun pause() {
  }

  override fun resume() {
  }

  open fun resize(width: Int, height: Int) {
  }
}