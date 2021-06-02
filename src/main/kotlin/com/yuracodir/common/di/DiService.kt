package com.yuracodir.common.di

import kotlin.reflect.KClass

class DiService {
  val modules = mutableListOf<DiModule>()

  inline fun <reified T> get(): T {
    val cls = T::class
    val item = modules.first { it.items.keys.contains(cls) }.items[cls]?.value
    return item as T
  }

  fun modules(vararg module: DiModule) {
    modules.addAll(module)
  }
}

class DiModule {
  val items = mutableMapOf<KClass<*>, Lazy<Any>>()

  inline fun <reified T> single(crossinline block: () -> T) {
    items[T::class] = lazy {
      block.invoke() as Any
    }
  }

  inline fun<reified T> get() : T = com.yuracodir.common.di.get()
}

fun module(block: DiModule.() -> Unit): DiModule {
  return DiModule().apply(block)
}

fun startDi(block: DiService.() -> Unit): DiService {
  return diInstance.apply(block)
}

val diInstance = DiService()

inline fun <reified T> get() = diInstance.get<T>()

inline fun <reified T> inject(): Lazy<T> = lazy {
  get()
}