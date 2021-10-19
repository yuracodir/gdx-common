package com.yuracodir.common.screens

import com.badlogic.gdx.scenes.scene2d.Touchable
import com.yuracodir.screens.Command
import com.yuracodir.screens.ContainerScreen
import com.yuracodir.screens.ScreenNavigator
import com.yuracodir.screens.ScreenRouter

open class GdxRouter(
  private val container: ContainerScreen,
  navigator: ScreenNavigator = ScreenNavigator(container)
) : ScreenRouter(container, navigator) {

  override fun applyCommand(vararg command: Command<*>) {
    if (isLocked()) return
    super.applyCommand(*command)
  }

  private fun isLocked() = (currentScreen as? GdxScreen)?.root?.touchable == Touchable.disabled
}