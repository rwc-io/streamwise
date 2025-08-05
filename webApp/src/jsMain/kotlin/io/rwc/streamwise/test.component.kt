package io.rwc.streamwise

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import external.ChartData
import external.ChartDataset
import external.Luxon
import kangular.core.Signal

@OptIn(ExperimentalJsExport::class)
@JsExport
class TestComponent {
  private val theNumber = BigDecimal.fromFloat(4.2f)

  private val aSignalK = Signal(theNumber)

  @Suppress("unused")
  val aSignal = aSignalK.ngSignal

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
  }
}