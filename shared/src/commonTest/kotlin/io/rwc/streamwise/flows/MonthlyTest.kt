package io.rwc.streamwise.flows

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MonthlyTest {

  @Test
  fun `test positive day offset`() {
    val value = BigDecimal.fromInt(100)
    val monthly = Monthly("test", 5, value)

    // Test a date that matches the offset
    val matchingDate = LocalDate(2023, 3, 5) // March 5, 2023
    assertEquals(value, monthly.valueOn(matchingDate))

    // Test a date that doesn't match the offset
    val nonMatchingDate = LocalDate(2023, 3, 6) // March 6, 2023
    assertEquals(BigDecimal.ZERO, monthly.valueOn(nonMatchingDate))
  }

  @Test
  fun `test negative day offset`() {
    val value = BigDecimal.fromInt(200)
    val monthly = Monthly("test", -1, value)

    // Test a date that matches the offset in a 31-day month
    val matchingDateMarch = LocalDate(2023, 3, 31) // March 31, 2023 (last day)
    assertEquals(value, monthly.valueOn(matchingDateMarch))

    // Test a date that matches the offset in a 30-day month
    val matchingDateApril = LocalDate(2023, 4, 30) // April 30, 2023 (last day)
    assertEquals(value, monthly.valueOn(matchingDateApril))

    // Test a date that doesn't match the offset
    val nonMatchingDate = LocalDate(2023, 3, 30) // March 30, 2023 (second-to-last day)
    assertEquals(BigDecimal.ZERO, monthly.valueOn(nonMatchingDate))
  }

  @Test
  fun `test February in leap year and non-leap year`() {
    val value = BigDecimal.fromInt(300)
    val monthly = Monthly("test", 29, value)

    // Test February 29 in a leap year
    val leapYearFeb29 = LocalDate(2020, 2, 29) // February 29, 2020 (leap year)
    assertEquals(value, monthly.valueOn(leapYearFeb29))

    // Test February 28 in a non-leap year (should match since February only has 28 days)
    val nonLeapYearFeb28 = LocalDate(2023, 2, 28) // February 28, 2023 (non-leap year)
    assertEquals(value, monthly.valueOn(nonLeapYearFeb28))

    // Test February 28 in a leap year (should not match)
    val leapYearFeb28 = LocalDate(2020, 2, 28) // February 28, 2020 (leap year)
    assertEquals(BigDecimal.ZERO, monthly.valueOn(leapYearFeb28))
  }

  @Test
  fun `test negative day offset in February`() {
    val value = BigDecimal.fromInt(400)
    val monthly = Monthly("test", -1, value)

    // Test last day of February in a leap year
    val leapYearFeb29 = LocalDate(2020, 2, 29) // February 29, 2020 (leap year)
    assertEquals(value, monthly.valueOn(leapYearFeb29))

    // Test last day of February in a non-leap year
    val nonLeapYearFeb28 = LocalDate(2023, 2, 28) // February 28, 2023 (non-leap year)
    assertEquals(value, monthly.valueOn(nonLeapYearFeb28))
  }

  @Test
  fun `test day offset exceeding month length`() {
    val value = BigDecimal.fromInt(500)
    val monthly = Monthly("test", 31, value)

    // Test a 31-day month
    val march31 = LocalDate(2023, 3, 31) // March 31, 2023
    assertEquals(value, monthly.valueOn(march31))

    // Test a 30-day month (should match on the last day)
    val april30 = LocalDate(2023, 4, 30) // April 30, 2023
    assertEquals(value, monthly.valueOn(april30))

    // Test February in a non-leap year (should match on the last day)
    val feb28 = LocalDate(2023, 2, 28) // February 28, 2023
    assertEquals(value, monthly.valueOn(feb28))
  }

  @Test
  fun `test zero day offset throws exception`() {
    val value = BigDecimal.fromInt(600)

    // Test that constructor throws an exception for zero day offset
    assertFailsWith<IllegalArgumentException> {
      Monthly("test", 0, value)
    }
  }

  @Test
  fun `test day offset longer than month throws exception`() {
    val value = BigDecimal.fromInt(700)

    // Test that constructor throws an exception for day offset longer than 31 days
    assertFailsWith<IllegalArgumentException> {
      Monthly("test", 32, value)
    }

    assertFailsWith<IllegalArgumentException> {
      Monthly("test", -32, value)
    }
  }
}
