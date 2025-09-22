package io.rwc.streamwise

import io.rwc.streamwise.flows.CashFlow
import io.rwc.streamwise.flows.Fixed
import io.rwc.streamwise.flows.accumulateFlows
import kangular.core.AngularWritable
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

@OptIn(ExperimentalJsExport::class)
@JsExport
class App(excludedFlowsNgSignal: dynamic) {
  private val excluded = AngularWritable<Set<CashFlow>>(excludedFlowsNgSignal)

  @Suppress("unused", "non_exportable_type")
  fun realizeFlowsToBalances(
    flows: Array<CashFlow>,
    excluded: Set<CashFlow>,
  ): Array<Fixed> {
    val startDate = LocalDate(2025, 9, 13)
    val endDate = startDate.plus(DatePeriod(years = 2))

    val filtered = flows.filterNot { flow ->
      isExcluded(flow, excluded)
    }

    return accumulateFlows(filtered.toList(), startDate, endDate).toTypedArray()
  }

  @Suppress("unused")
  fun onExcludedFlows(set: Set<CashFlow>) {
    excluded.set(set)
  }

  private fun isExcluded(flow: CashFlow, excluded: Set<CashFlow>): Boolean {
    return excluded.contains(flow)
  }
}