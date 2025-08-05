package external

import js.array.ReadonlyArray

@JsModule("luxon")
external object ChartJs {
  object DateTime {
    fun local(year: Int, month: Int, day: Int): dynamic
    fun local(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): dynamic
  }
}

@OptIn(ExperimentalJsExport::class, ExperimentalJsCollectionsApi::class)
@JsExport
class ChartDataset(
  val label: String? = null,
  val data: ReadonlyArray<Number> = arrayOf(),
  val fill: Boolean? = null,
  val borderWidth: Int? = null,
  val backgroundColor: String? = null,
  val borderColor: String? = null,
  val tension: Float? = null,
)

@OptIn(ExperimentalJsCollectionsApi::class, ExperimentalJsExport::class)
class ChartData(
  @JsName("labels") val labels: Array<Any>,
  @JsName("datasets") val datasets: Array<ChartDataset>,
)

class ChartOptions(
  @JsName("scales") val scales: Map<String, Map<String, Any>>? = null,
)
