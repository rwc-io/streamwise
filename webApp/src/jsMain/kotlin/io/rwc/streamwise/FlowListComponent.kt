package io.rwc.streamwise

import io.rwc.streamwise.flows.CashFlow
import io.rwc.streamwise.flows.describe
import kangular.core.AngularWritable

@OptIn(ExperimentalJsExport::class)
@JsExport
class FlowListComponent(excludedFlowsNgSignal: dynamic) {
  // For communicating the output:
  private val excludedFlowsSignal = AngularWritable<Set<CashFlow>>(excludedFlowsNgSignal)

  // For internal state tracking:
  private val excludedFlows: MutableSet<CashFlow> = mutableSetOf()

  init {
    // Initialize the output signal with the empty set
    excludedFlowsSignal.set(excludedFlows)
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
  fun toggle(flow: CashFlow) {
    if (excludedFlows.contains(flow)) {
      excludedFlows.remove(flow)
    } else {
      excludedFlows.add(flow)
    }

    excludedFlowsSignal.set(excludedFlows.toSet())
  }

  @Suppress("unused")
  fun isExcluded(flow: CashFlow): Boolean {
    return excludedFlows.contains(flow)
  }
}