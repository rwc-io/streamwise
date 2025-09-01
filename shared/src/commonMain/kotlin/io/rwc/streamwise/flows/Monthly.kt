package io.rwc.streamwise.flows

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.serialization.kotlinx.bigdecimal.BigDecimalHumanReadableSerializer
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.yearMonth
import kotlinx.serialization.Serializable
import kotlin.math.abs


/**
 * Implementation of CashFlow that occurs on a specific day of the month.
 *
 * @param dayOffset The day offset (1-indexed). If positive, it's the day from the start of the month.
 *                  If negative, it's the day from the end of the month.
 * @param amount The value of the cash flow.
 */
@Serializable
data class Monthly(
  val name: String,
  private val dayOffset: Int,
  @Serializable(with = BigDecimalHumanReadableSerializer::class)
  private val amount: BigDecimal,
) : CashFlow {
  init {
    require(dayOffset != 0) { "Day offset cannot be zero" }
    require(abs(dayOffset) <= 31) { "Day offset can't be longer than a month" }
  }

  override fun valueOn(date: LocalDate): BigDecimal {
    val targetDay = calculateTargetDay(date.yearMonth)

    return if (date.day == targetDay) {
      amount
    } else {
      BigDecimal.ZERO
    }
  }

  /**
   * Calculates the target day of the month based on the day offset.
   *
   * @param month The month
   * @param year The year (needed to determine the number of days in the month)
   * @return The target day of the month
   */
  private fun calculateTargetDay(yearMonth: YearMonth): Int {
    val lastDayOfMonth = yearMonth.lastDay

    return if (dayOffset > 0) {
      // Positive offset: count from the beginning of the month
      minOf(dayOffset, lastDayOfMonth.day)
    } else {
      // Negative offset: count from the end of the month
      val daysFromEnd = -dayOffset
      val targetDay = lastDayOfMonth.day - daysFromEnd + 1
      maxOf(targetDay, 1)
    }
  }
}
