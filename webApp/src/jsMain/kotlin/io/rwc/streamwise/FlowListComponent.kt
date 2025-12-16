package io.rwc.streamwise

import io.rwc.streamwise.flows.CashFlow
import io.rwc.streamwise.flows.Fixed
import io.rwc.streamwise.flows.FlowsDbJs
import io.rwc.streamwise.flows.describe
import kangular.core.AngularWritable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
  fun toggleFlow(flow: CashFlow) {
    if (excludedFlows.contains(flow)) {
      excludedFlows.remove(flow)
    } else {
      excludedFlows.add(flow)
    }

    excludedFlowsSignal.set(excludedFlows.toSet())
  }

  @Suppress("unused")
  fun editFlow(flow: CashFlow) {
    if (flow is Fixed) {
      val toggled = flow.copy(amount = flow.amount * -1)
      CoroutineScope(Dispatchers.Default).launch {
        FlowsDbJs.saveFlow(StreamFire.instance.firestore, toggled)
      }
    }
  }

  @Suppress("unused")
  fun isExcluded(flow: CashFlow): Boolean {
    return excludedFlows.contains(flow)
  }
}