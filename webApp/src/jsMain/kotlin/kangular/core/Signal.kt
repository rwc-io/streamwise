package kangular.core

import kangular.external.AngularCore

interface Signal<T> {
  val value: T
}

class ReadonlySignal<T>(val ngSignal: dynamic) : Signal<T> {
  override val value: T
    get() = ngSignal()
}

class WritableSignal<T>(val ngSignal: dynamic) : Signal<T> {
  @JsName("altcon")
  constructor(initialValue: T) : this(AngularCore.signal(initialValue))

  override var value: T
    get() = ngSignal()
    set(value) = ngSignal.set(value)
}
