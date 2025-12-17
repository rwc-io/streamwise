package kangular.external

@JsModule("@angular/material/dialog")
@JsNonModule
external object MatDialog {
  fun <ResultType> open(component: dynamic, options: dynamic): MatDialogRef<ResultType>
}

external class MatDialogRef<T> {
  fun afterClosed(): MatDialogObservable<T>
}

external class MatDialogObservable<T> {
  fun subscribe(listener: (T) -> Unit)
}