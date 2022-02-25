package com.yuracodir.common.application

import com.yuracodir.common.resources.Resources

open class Context(
  val resources: Resources,
  val display: Display
) {
  constructor(context: Context) : this(context.resources, context.display)

  fun dispose() {
    resources.dispose()
  }
}

class Display(
  var width: Int,
  var height: Int,
  val viewportWidth: Float,
  val viewportHeight: Float,
  val density: Float = viewportWidth / width
) {
  constructor(viewportWidth: Float, viewportHeight: Float) : this(0, 0, viewportWidth, viewportHeight)

  fun setSize(width: Int, height: Int) {
    this.width = width
    this.height = height
  }
}