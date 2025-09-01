package io.rwc.streamwise

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import external.ChartData
import external.ChartDataset
import io.rwc.streamwise.flows.Fixed
import kangular.core.Computed
import kangular.core.Signal
import kangular.external.AngularCore.effect
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalJsExport::class)
@JsExport
class TestComponent {
  private val dataService = StreamFire.dataService

  @Suppress("unused")
  val flowBundles = dataService.flowBundles

  private val startDate = LocalDate(2025, 1, 1)
  private val endDate = LocalDate(2025, 12, 31)

  private val balances = Signal(
    listOf<Fixed>()
  )

  private val flowBundleService = FlowBundleService()

  @Suppress("unused")
  val chartData = Computed {
    ChartData(
      labels = balances().map { it.date.toString() }.toTypedArray(),
      datasets = arrayOf(
        ChartDataset(
          label = "Balance",
          data = balances().map { it.amount.toStringExpanded() }.toTypedArray(),
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

  init {
    effect {
      console.log("Recomputing balances")
      flowBundleService.start(
        targetBalances = balances,
        bundlesSignal = flowBundles,
        startDate = startDate,
        endDate = endDate,
      )
    }
  }
}