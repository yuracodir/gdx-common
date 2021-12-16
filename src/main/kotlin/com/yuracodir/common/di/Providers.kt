package com.yuracodir.common.di


inline fun <reified T : Any> DiModule.factory(noinline block: DiService.() -> T) =
  registerProvider(T::class, FactoryDiProvider(block))

class FactoryDiProvider<T>(private val block: DiService.() -> T) : DiProvider<T> {
  override fun getInstance(service: DiService): T {
    return block.invoke(service)
  }

  override fun drop() {

  }
}

inline fun <reified T : Any> DiModule.single(noinline block: DiService.() -> T) =
  registerProvider(T::class, SingleDiProvider(block))


class SingleDiProvider<T>(private val block: DiService.() -> T) : ClosableDiProvider<T>() {
  private var value: T? = null

  override fun getInstance(service: DiService): T {
    val v = value
    return if (v == null) {
      val instance = block.invoke(service)
      value = instance
      instance
    } else {
      v
    }
  }

  override fun drop() {
    value?.also {
      closeCallback?.invoke(it)
    }
  }

}
