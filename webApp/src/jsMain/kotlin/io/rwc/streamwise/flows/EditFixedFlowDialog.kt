package io.rwc.streamwise.flows

import external.luxon.DateTime


@OptIn(ExperimentalJsExport::class)
@JsExport
class EditFixedFlowDialogData(val date: DateTime, val amount: String)