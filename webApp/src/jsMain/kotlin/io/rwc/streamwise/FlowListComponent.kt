package io.rwc.streamwise

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import external.luxon.DateTime
import external.luxon.toLocalDate
import io.rwc.streamwise.flows.*
import kangular.core.AngularWritable
import kangular.external.MatDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalJsExport::class)
@JsExport
class FlowListComponent(
  ngDialog: MatDialog,
  private val editFixedFlowModalComponent: dynamic,
  excludedFlowsNgSignal: dynamic,
) {
  // For communicating the output:
  private val excludedFlowsSignal = AngularWritable<Set<CashFlow>>(excludedFlowsNgSignal)

  // For internal state tracking:
  private val excludedFlows: MutableSet<CashFlow> = mutableSetOf()

  private val dialog = kangular.material.dialog.MatDialog(ngDialog)

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
      val formData = EditFixedFlowDialogData(
        date = DateTime.fromISO(flow.date.toString()),
        amount = flow.amount.toStringExpanded(),
      )

      val x = dialog.open<Any, EditFixedFlowDialogData?>(editFixedFlowModalComponent, formData)
      x.afterClosed().subscribe { result ->
        if (result == null) return@subscribe

        val newFlow = flow.copy(
          date = result.date.toLocalDate(),
          amount = BigDecimal.parseString(result.amount),
        )

        CoroutineScope(Dispatchers.Default).launch {
          FlowsDbJs.saveFlow(StreamFire.instance.firestore, newFlow)
        }
      }
    }
  }

  @Suppress("unused")
  fun isExcluded(flow: CashFlow): Boolean {
    return excludedFlows.contains(flow)
  }
}