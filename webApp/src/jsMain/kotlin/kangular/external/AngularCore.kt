package kangular.external

@JsModule("@angular/core")
@JsNonModule
external object AngularCore {
  // So far, we need to let Angular manage all its core functions
  // Otherwise, state lifecycle management doesn't work properly
}
