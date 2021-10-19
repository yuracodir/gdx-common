package com.yuracodir.common.screens

import com.badlogic.gdx.scenes.scene2d.Group
import com.yuracodir.screens.CallbackScreen
import com.yuracodir.screens.ScreenRouter

abstract class GdxScreen<R: ScreenRouter>(override var router: R) : CallbackScreen<R>() {

    abstract val root: Group

    override fun getName(): String = javaClass.simpleName

    override fun back(): Boolean {
        return super.back() && router.back()
    }

    open fun resize(width: Int, height: Int) {}
}