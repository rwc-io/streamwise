package io.rwc.streamwise.flows

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.serialization.kotlinx.bigdecimal.BigDecimalHumanReadableSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

/**
 * A cash flow that occurs on a yearly cadence, on the anniversary of the start date.
 *
 * The occurrence interval is (skip + 1) years.
 * - skip = 0 → every year
 * - skip = 1 → every 2 years
 * - skip = 2 → every 3 years
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class Yearly(
  val name: String,
  val startDate: LocalDate,
  @Serializable(with = BigDecimalHumanReadableSerializer::class)
  val amount: BigDecimal,
  val skip: Int = 0,
) : CashFlow {
  init {
    require(skip >= 0) { "Skip cannot be negative" }
  }

  override fun valueOn(date: LocalDate): BigDecimal {
    if (date < startDate) return BigDecimal.ZERO

    val intervalYears = skip + 1
    val yearsFromStart = date.year - startDate.year
    if (yearsFromStart % intervalYears != 0) return BigDecimal.ZERO

    val anniversary = anniversaryInYear(date.year)
    return if (date == anniversary) amount else BigDecimal.ZERO
  }

  private fun anniversaryInYear(year: Int): LocalDate {
    return if (startDate.monthNumber == 2 && startDate.dayOfMonth == 29) {
      // For Feb 29 starts, use Feb 29 on leap years, otherwise Feb 28
      if (isLeapYear(year)) LocalDate(year, 2, 29) else LocalDate(year, 2, 28)
    } else {
      LocalDate(year, startDate.monthNumber, startDate.dayOfMonth)
    }
  }

  private fun isLeapYear(year: Int): Boolean {
    // Constructing Feb 29 will throw on non-leap years; catch to determine
    return try {
      LocalDate(year, 2, 29)
      true
    } catch (e: IllegalArgumentException) {
      false
    }
  }
}
