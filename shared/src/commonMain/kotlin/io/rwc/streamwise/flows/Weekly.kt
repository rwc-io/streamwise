package io.rwc.streamwise.flows

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.serialization.kotlinx.bigdecimal.BigDecimalHumanReadableSerializer
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

/**
 * A cash flow that occurs on a weekly cadence, starting from a given date.
 *
 * The occurrence interval is 7 * (skip + 1) days.
 * - skip = 0 → every week
 * - skip = 1 → every 2 weeks
 * - skip = 2 → every 3 weeks
 */
@Serializable
data class Weekly(
  val name: String,
  val startDate: LocalDate,
  @Serializable(with = BigDecimalHumanReadableSerializer::class)
  val amount: BigDecimal,
  val skip: Int = 0,
  val id: String = "",
) : CashFlow {
  init {
    require(skip >= 0) { "Skip cannot be negative" }
  }

  override fun valueOn(date: LocalDate): BigDecimal {
    if (date < startDate) return BigDecimal.ZERO

    val intervalDays = 7 * (skip + 1)
    val daysFromStart = startDate.daysUntil(date)

    return if (daysFromStart % intervalDays == 0) amount else BigDecimal.ZERO
  }
}
