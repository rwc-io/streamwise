package io.rwc.streamwise

import io.rwc.streamwise.flows.CashFlow
import io.rwc.streamwise.flows.FlowBundle
import io.rwc.streamwise.flows.describe
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
    return try {
      flow.describe()
    } catch (e: Exception) {
      "Unknown flow type"
    }
  }

  @Suppress("unused")
  fun ngOnDestroy() {
    flowBundleService.stop()
  }
}