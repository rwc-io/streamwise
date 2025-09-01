package kangular.core

import kangular.external.AngularCore.computed

@OptIn(ExperimentalJsExport::class)
@JsExport
class Computed<T>(computation: () -> T) {
  val ngComputed: dynamic = computed(computation)

  operator fun invoke(): T {
    return ngComputed()
  }

  val value: T
    get() = ngComputed()
}