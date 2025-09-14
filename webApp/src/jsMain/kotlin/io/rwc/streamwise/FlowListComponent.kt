package io.rwc.streamwise

import io.rwc.streamwise.flows.CashFlow
import io.rwc.streamwise.flows.FlowBundle
import kangular.core.AngularWritable

@OptIn(ExperimentalJsExport::class)
@JsExport
class FlowListComponent(ngFlowsSignal: dynamic) {
  private val flowsSignal = AngularWritable<Array<CashFlow>>(ngFlowsSignal)
  private val flowBundleService = FlowBundleService()

  @Suppress("unused")
  fun listenToFlows(bundles: Array<FlowBundle>) {
    flowBundleService.bundlesToFlows(bundles, flowsSignal)
  }

  @Suppress("unused", "non_exportable_type")
  fun describe(flow: CashFlow): String {
    return when (flow) {
      is io.rwc.streamwise.flows.Fixed -> "Fixed on ${flow.date}: ${flow.amount}"
      is io.rwc.streamwise.flows.Monthly -> "Monthly ${flow.name} on day offset ${flow.dayOffset}: ${flow.amount}"
      else -> "Unknown flow type"
    }
  }

  @Suppress("unused")
  fun ngOnDestroy() {
    flowBundleService.stop()
  }
}