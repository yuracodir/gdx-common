package com.yuracodir.common.di

import kotlin.reflect.KClass

class DiService {
  val modules = mutableListOf<DiModule>()

  inline fun <reified T> get(): T {
    val cls = T::class
    return modules.first { it.items.keys.contains(cls) }.items[cls]?.getInstance(this) as? T
      ?: throw IllegalStateException("Dependency $cls is not provided!")
  }

  fun modules(vararg module: DiModule) {
    modules.addAll(module.toList())
  }

  fun unload(vararg module: DiModule) {
    modules.removeAll(module.toList())
    module.forEach {
      it.drop()
    }
  }
}

class DiModule {
  val items = mutableMapOf<KClass<*>, DiProvider<*>>()

  fun <P : DiProvider<T>, T : Any> registerProvider(cls: KClass<T>, provider: P) = provider.also {
    items[cls] = provider
  }

  fun drop() {
    items.values.forEach {
      it.drop()
    }
  }
}

infix fun <T> ClosableDiProvider<T>.onClose(close: (T) -> Unit) {
  this.closeCallback = close
}

abstract class ClosableDiProvider<T> : DiProvider<T> {
  internal var closeCallback: ((T) -> Unit)? = null
}

interface DiProvider<T> {
  fun getInstance(service: DiService): T
  fun drop()
}

fun module(block: DiModule.() -> Unit): DiModule {
  return DiModule().apply(block)
}

fun withDi(block: DiService.() -> Unit): DiService {
  return diInstance.apply(block)
}

val diInstance = DiService()

inline fun <reified T> DiInjects.inject(): Lazy<T> = lazy {
  diInstance.get()
}

interface DiInjects