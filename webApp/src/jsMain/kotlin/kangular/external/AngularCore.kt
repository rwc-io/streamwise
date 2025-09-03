package kangular.external

@JsModule("@angular/core")
@JsNonModule
external object AngularCore {
  val computed: dynamic
  val effect: dynamic
  fun <T> signal(initialValue: T): () -> T
}
