package com.yuracodir.common.screens

import com.badlogic.gdx.scenes.scene2d.Touchable
import com.yuracodir.screens.ContainerScreen
import com.yuracodir.screens.Route
import com.yuracodir.screens.Screen
import com.yuracodir.screens.ScreenNavigator
import com.yuracodir.screens.ScreenRouter

abstract class GdxRouter<T : Route>(
  private val container: ContainerScreen,
  navigator: ScreenNavigator = ScreenNavigator(container)
) : ScreenRouter<T>(container, navigator) {

  protected fun isLocked() = (currentScreen as? GdxScreen)?.root?.touchable == Touchable.disabled

  override fun setScreen(screen: Screen) {
    if (isLocked()) return
    super.setScreen(screen)
  }
}