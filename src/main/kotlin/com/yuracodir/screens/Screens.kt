package com.yuracodir.screens

interface ContainerScreen {
  val childRouter: ScreenRouter<*>
  fun attach(screen: Screen)
  fun detach(screen: Screen)
}

interface ScreenCallback {
  fun onCreate() {}
  fun onPause() {}
  fun onResume() {}
  fun onDestroy() {}
  fun onBackPressed() {}
}

interface Screen {
  fun create() {}
  fun resume() {}
  fun pause() {}
  fun destroy() {}
  fun getName(): String = ""
  fun onBackPressed() {}
}

open class ScreenNavigator(private val containerScreen: ContainerScreen) {
  var lastScreen: Screen? = null
  private val screenStates = mutableMapOf<Screen, State>()

  enum class State {
    Created, Resumed, Paused
  }

  open fun changeScreen(toScreen: Screen) {
    lastScreen?.let {
      pause(it)
      detach(it)
      destroy(it)
    }
    lastScreen = toScreen
    create(toScreen)
    attach(toScreen)
    resume(toScreen)
  }

  open fun detach(screen: Screen) {
    containerScreen.detach(screen)
  }

  open fun attach(screen: Screen) {
    containerScreen.attach(screen)
  }

  open fun create(screen: Screen? = lastScreen) {
    screen?.let {
      val state = screenStates[it]
      if (state == null) {
        screenStates[it] = State.Created
        it.create()
      }
    }
  }

  open fun resume(screen: Screen? = lastScreen) {
    screen?.let {
      val state = screenStates[it]
      if (state == State.Created || state == State.Paused) {
        screenStates[it] = State.Resumed
        it.resume()
      }
    }
  }

  open fun pause(screen: Screen? = lastScreen) {
    screen?.let {
      val state = screenStates[it]
      if (state == State.Resumed) {
        screenStates[it] = State.Paused
        it.pause()
      }
    }
  }

  open fun destroy(screen: Screen? = lastScreen) {
    screen?.let {
      val state = screenStates[it]
      if (state == State.Paused || state == State.Created) {
        screenStates.remove(it)
        it.destroy()
      }
    }
  }
}

abstract class ScreenRouter<T : Route>(
  containerScreen: ContainerScreen,
  open val navigator: ScreenNavigator = ScreenNavigator(containerScreen)
) : Router<T>() {
  val currentScreen
    get() = navigator.lastScreen

  open fun setScreen(screen: Screen) {
    navigator.changeScreen(screen)
  }
}