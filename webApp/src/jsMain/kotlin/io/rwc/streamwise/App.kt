package io.rwc.streamwise

import io.rwc.streamwise.flows.Fixed
import io.rwc.streamwise.flows.FlowBundle
import kangular.core.AngularWritable
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

@OptIn(ExperimentalJsExport::class)
@JsExport
class App() {
  private val flowsRealizer = FlowsRealizerService()

  @Suppress("unused")
  fun ngOnDestroy() {
    flowsRealizer.stop()
  }

  @Suppress("unused")
  fun realizeFlowsToBalances(ngBalancesSignal: dynamic, flowBundles: Array<FlowBundle>) {
    flowsRealizer.stop()
    val balancesSignal = AngularWritable<Array<Fixed>>(ngBalancesSignal)
    val startDate = LocalDate(2025, 9, 13)
    val endDate = startDate.plus(DatePeriod(years = 2))
    flowsRealizer.bundlesToBalances(
      targetBalances = balancesSignal,
      bundles = flowBundles,
      startDate = startDate,
      endDate = endDate,
    )
  }
}