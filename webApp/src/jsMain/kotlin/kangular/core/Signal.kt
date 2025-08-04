package kangular.core

import kangular.external.AngularCore

/**
 * A typed wrapper for Angular's signals.
 * Should we be generating types from the typescript definitions?
 * I don't think that works, b/c we can't re-type the call signature (??)
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
class Signal<T>(val initialValue: T) {
  val ngSignal: dynamic = AngularCore.signal(initialValue)

  // Note that this only applies in Kotlin.
  // A kotlin Signal<T> property type can't be 'invoked' from JS.
  // (JS has to invoke the ngSignal property)
  operator fun invoke(): T {
    return ngSignal()
  }

  fun set(value: T) {
    ngSignal.set(value)
  }
}