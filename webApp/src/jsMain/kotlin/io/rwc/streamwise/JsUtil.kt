package io.rwc.streamwise

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import js.intl.NumberFormat

fun jsObjectOf(vararg pairs: Pair<String, dynamic>): dynamic {
  val obj: dynamic = Any()
  pairs.forEach {
      (key, value) ->
    obj[key] = value
  }
  return obj
}

fun toCurrency(value: Any): String {
  val formatter = NumberFormat(
    "en-US",
    jsObjectOf("style" to "currency", "currency" to "USD")
  )
  return when (value) {
    is BigDecimal -> formatter.format(value.toStringExpanded())
    is Number -> formatter.format(value)
    is String -> formatter.format(value)
    else -> "unknown type for value: $value"
  }
}
