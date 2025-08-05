package io.rwc.streamwise

val timeAxisX = jsObjectOf(
  "type" to "time",
  "time" to jsObjectOf(
    "unit" to "day",
    "displayFormats" to jsObjectOf(
      "day" to "MMM d, yyyy"
    )
  ),
  "ticks" to jsObjectOf(
    "source" to "auto",
    "autoSkip" to true,
    "maxTicksLimit" to 20
  )
)

val currencyAxisY = jsObjectOf(
  "beginAtZero" to true,
  "ticks" to jsObjectOf(
    "callback" to { value: dynamic ->
      toCurrency(value)
    }
  )
)

val currencyTooltip = jsObjectOf(
  "callbacks" to jsObjectOf(
    "label" to { context: dynamic ->
      var label = context.dataset.label ?: ""
      if (label) {
        label += ": "
      }
      if (context.parsed.y != null) {
        label += toCurrency(context.parsed.y)
      }
      label
    }
  ),
)
