package io.rwc.streamwise

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import external.ChartData
import external.ChartDataset
import io.rwc.streamwise.flows.Fixed
import io.rwc.streamwise.flows.FlowBundle
import kangular.core.AngularWritable
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

@OptIn(ExperimentalJsExport::class)
@JsExport
class TestComponent(ngBalancesSignal: dynamic) {
  private val balancesSignal = AngularWritable<Array<Fixed>>(ngBalancesSignal)

  private val startDate = LocalDate(2025, 9, 13)
  private val endDate = startDate.plus(DatePeriod(years = 2))

  private val flowBundleService = FlowBundleService()

  @Suppress("unused", "non_exportable_type")
  fun computeChartData(balances: Array<Fixed>): dynamic {
    return ChartData(
      labels = balances.map { it.date.toString() }.toTypedArray(),
      datasets = arrayOf(
        ChartDataset(
          label = "Balance",
          data = balances.map { it.amount.toStringExpanded() }.toTypedArray(),
          backgroundColor = "#4bc0c0ff",
          borderWidth = 1,
          fill = false,
          borderColor = "#4bc0c0ff"
        )
      )
    ).toJs()
  }

  @Suppress("unused")
  val barChartOptions = jsObjectOf(
    "scales" to jsObjectOf(
      "x" to timeAxisX,
      "y" to currencyAxisY,
    ),
    "plugins" to jsObjectOf(
      "legend" to jsObjectOf(
        "display" to true,
        "position" to "top"
      ),
      "tooltip" to currencyTooltip
    )
  )

  @Suppress("unused")
  fun ngOnInit() {
    BigDecimal.useToStringExpanded = true
  }

  @Suppress("unused")
  fun ngOnDestroy() {
    flowBundleService.stop()
  }

  @Suppress("unused")
  fun listenToBalances(flowBundles: Array<FlowBundle>) {
    console.log("Recomputing balances")
    flowBundleService.start(
      targetBalances = balancesSignal,
      bundles = flowBundles,
      startDate = startDate,
      endDate = endDate,
    )
  }
}