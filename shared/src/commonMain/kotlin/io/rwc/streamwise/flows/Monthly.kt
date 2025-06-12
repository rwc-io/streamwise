package io.rwc.streamwise.flows

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.number
import kotlin.math.abs

/**
 * A simple class to represent a year and month combination.
 *
 * @param year The year
 * @param month The month
 */
private class YearMonth(val year: Int, val month: Month) {
    /**
     * Returns the last day of the month.
     *
     * @return The date representing the last day of the month
     */
    fun atEndOfMonth(): LocalDate {
        val daysInMonth = when (month) {
            Month.FEBRUARY -> if (isLeapYear(year)) 29 else 28
            Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
            else -> 31
        }
        return LocalDate(year, month, daysInMonth)
    }

    /**
     * Determines if the given year is a leap year.
     *
     * @return True if the year is a leap year, false otherwise
     */
    private fun isLeapYear(year: Int): Boolean {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
    }
}

/**
 * Implementation of CashFlow that occurs on a specific day of the month.
 * 
 * @param dayOffset The day offset (1-indexed). If positive, it's the day from the start of the month.
 *                  If negative, it's the day from the end of the month.
 * @param value The value of the cash flow.
 */
class Monthly(val name: String, private val dayOffset: Int, private val value: BigDecimal) : CashFlow {
    init {
        require(dayOffset != 0) { "Day offset cannot be zero" }
        require(abs(dayOffset) <= 31) { "Day offset can't be longer than a month" }
    }

    override fun valueOn(date: LocalDate): BigDecimal {
        val targetDay = calculateTargetDay(date.month, date.year)

        return if (date.dayOfMonth == targetDay) {
            value
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
    private fun calculateTargetDay(month: Month, year: Int): Int {
        val yearMonth = YearMonth(year, month)
        val lastDayOfMonth = yearMonth.atEndOfMonth()

        return if (dayOffset > 0) {
            // Positive offset: count from the beginning of the month
            minOf(dayOffset, lastDayOfMonth.dayOfMonth)
        } else {
            // Negative offset: count from the end of the month
            val daysFromEnd = -dayOffset
            val targetDay = lastDayOfMonth.dayOfMonth - daysFromEnd + 1
            maxOf(targetDay, 1)
        }
    }
}
