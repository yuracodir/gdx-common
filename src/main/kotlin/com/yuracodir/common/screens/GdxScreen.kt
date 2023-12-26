package com.yuracodir.common.screens

import com.badlogic.gdx.scenes.scene2d.Group
import com.yuracodir.screens.CallbackScreen

abstract class GdxScreen : CallbackScreen() {

  abstract val root: Group

  var backIsNotDispatched = false
  override fun getName(): String = javaClass.simpleName

  open fun resize(width: Int, height: Int) {}

  open fun dispatchOnBackPressed() {
    super.onBackPressed()
    onBackPressed()
  }

  override fun onBackPressed() {
    backIsNotDispatched = true
  }
}