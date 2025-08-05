package io.rwc.streamwise

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import external.ChartData
import external.ChartDataset
import external.Luxon
import io.rwc.streamwise.flows.Fixed
import kangular.core.Signal
import kangular.external.AngularCore.computed
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

  private val balances = Signal(
    listOf(
      Fixed(
        LocalDate(2025, 1, 1),
        BigDecimal.fromFloat(1000f)
      ),
      Fixed(
        LocalDate(2025, 1, 2),
        BigDecimal.fromFloat(1200f)
      ),
      Fixed(
        LocalDate(2025, 1, 3),
        BigDecimal.fromFloat(900f)
      ),
    )
  )

  val chartData = computed {
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
    )
  }

  val barChartData = ChartData(
    labels = arrayOf(
      Luxon.DateTime.local(2025, 1, 1),
      Luxon.DateTime.local(2025, 1, 2),
      Luxon.DateTime.local(2025, 1, 3),
      Luxon.DateTime.local(2025, 1, 4),
      Luxon.DateTime.local(2025, 1, 5),
      Luxon.DateTime.local(2025, 1, 6)
    ),
    datasets = arrayOf(
      ChartDataset(
        label = "Balance",
        data = arrayOf(1200, 1900, 300, 500, 200, 300),
        backgroundColor = "#4bc0c0ff",
        borderWidth = 1,
        fill = false,
        borderColor = "#4bc0c0ff"
      )
    )
  )

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
    println("TestComponent initialized with number: $theNumber")
    BigDecimal.useToStringExpanded = true
  }

  @Suppress("unused")
  fun increment() {
    val x = aSignalK()
    aSignalK.set(x + 1)

    // Add a random balance entry to the end of the balances
    val oldBalances = balances()
    val lastDate = oldBalances.maxOfOrNull { it.date }
    val nextDate = lastDate?.plus(1, kotlinx.datetime.DateTimeUnit.DAY) ?: LocalDate(2025, 1, 4)
    val nextBalance = Random.nextFloat() * 2000
    balances.set(
      oldBalances + Fixed(
        nextDate,
        BigDecimal.fromFloat(nextBalance)
      )
    )
  }
}