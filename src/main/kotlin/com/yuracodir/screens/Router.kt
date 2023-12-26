package com.yuracodir.screens


abstract class Router<T : Route> {

  protected val history = ArrayDeque<T>()

  override fun toString() = history.toString()
  fun isEmpty() = history.size == 0
  fun size() = history.size

  fun pop(): T? {
    return if (history.size > 1) {
      history.removeLast()
    } else null
  }

  fun push(key: T): T {
    history.add(key)
    return key
  }

  fun commit() {
    onChanged(history.last())
  }

  fun open(key: T) {
    push(key)
    commit()
  }

  protected abstract fun onChanged(key: T)
}

interface Route