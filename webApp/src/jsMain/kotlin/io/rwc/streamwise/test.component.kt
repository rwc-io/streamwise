package io.rwc.streamwise

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import external.ChartData
import external.ChartDataset
import io.rwc.streamwise.flows.Fixed
import io.rwc.streamwise.flows.makeSomeBalances
import kangular.core.Computed
import kangular.core.Signal
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import kotlin.random.Random

@OptIn(ExperimentalJsExport::class)
@JsExport
class TestComponent {
  private val theNumber = BigDecimal.fromFloat(4.2f)

  private val aSignalK = Signal(theNumber)

  @Suppress("unused")
  val aSignal = aSignalK.ngSignal

  private val dataService = StreamFire.dataService

  @Suppress("unused")
  val flowBundles = dataService.flowBundles

  private val balances = Signal(
    makeSomeBalances(LocalDate(2025, 1, 1), LocalDate(2025, 7, 1))
  )

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
  fun increment() {
    val x = aSignalK()
    aSignalK.value = x + 1

    // Add a random balance entry to the end of the balances
    val oldBalances = balances()
    val lastDate = oldBalances.maxOfOrNull { it.date }
    val nextDate = lastDate?.plus(1, kotlinx.datetime.DateTimeUnit.DAY) ?: LocalDate(2025, 1, 4)
    val nextBalance = Random.nextFloat() * 2000
    balances.value = oldBalances + Fixed(
      nextDate,
      BigDecimal.fromFloat(nextBalance)
    )
  }
}