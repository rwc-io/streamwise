package io.rwc.streamwise

import io.rwc.streamwise.flows.CashFlow
import io.rwc.streamwise.flows.describe

@OptIn(ExperimentalJsExport::class)
@JsExport
class FlowListComponent {
  @Suppress("unused", "non_exportable_type")
  fun describe(flow: CashFlow): String {
    return try {
      flow.describe()
    } catch (e: Exception) {
      "Unknown flow type"
    }
  }
}