package external

@OptIn(ExperimentalJsExport::class, ExperimentalJsCollectionsApi::class)
@JsExport
class ChartDataset(
  val label: String? = null,
  val data: Array<Any> = arrayOf(),
  val fill: Boolean? = null,
  val borderWidth: Int? = null,
  val backgroundColor: String? = null,
  val borderColor: String? = null,
  val tension: Float? = null,
) {
  fun toJs(): dynamic {
    val obj: dynamic = Any()
    if (label != null) obj["label"] = label
    obj["data"] = data
    if (fill != null) obj["fill"] = fill
    if (borderWidth != null) obj["borderWidth"] = borderWidth
    if (backgroundColor != null) obj["backgroundColor"] = backgroundColor
    if (borderColor != null) obj["borderColor"] = borderColor
    if (tension != null) obj["tension"] = tension
    return obj
  }
}

@OptIn(ExperimentalJsCollectionsApi::class, ExperimentalJsExport::class)
@JsExport
class ChartData(
  @JsName("labels") val labels: Array<Any>,
  @JsName("datasets") val datasets: Array<ChartDataset>,
) {
  fun toJs(): dynamic {
    val obj: dynamic = Any()
    obj["labels"] = labels
    obj["datasets"] = datasets.map { it.toJs() }.toTypedArray()
    return obj
  }
}

class ChartOptions(
  @JsName("scales") val scales: Map<String, Map<String, Any>>? = null,
)
