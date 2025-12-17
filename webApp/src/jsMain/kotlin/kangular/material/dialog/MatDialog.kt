package kangular.material.dialog

import kangular.external.MatDialogRef

class MatDialog(val ngDialog: kangular.external.MatDialog) {
  fun <DataType, ResultType> open(component: dynamic, data: DataType?): MatDialogRef<ResultType> {
    val opts: dynamic = Any()
    if (data != null) {
      opts["data"] = data
    }
    return ngDialog.open(component, opts)
  }
}