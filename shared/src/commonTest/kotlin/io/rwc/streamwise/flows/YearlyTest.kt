package io.rwc.streamwise.flows

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class YearlyTest {

  @Test
  fun `occurs on start date and every year thereafter when skip is 0`() {
    val amount = BigDecimal.fromInt(100)
    val yearly = Yearly(name = "membership", startDate = LocalDate(2020, 5, 10), amount = amount, skip = 0)

    // On start date
    assertEquals(amount, yearly.valueOn(LocalDate(2020, 5, 10)))

    // Next years same day
    assertEquals(amount, yearly.valueOn(LocalDate(2021, 5, 10)))
    assertEquals(amount, yearly.valueOn(LocalDate(2022, 5, 10)))

    // Not on an occurrence
    assertEquals(BigDecimal.ZERO, yearly.valueOn(LocalDate(2021, 5, 11)))
  }

  @Test
  fun `occurs every 2 years when skip is 1`() {
    val amount = BigDecimal.fromInt(250)
    val yearly = Yearly(name = "biennial", startDate = LocalDate(2020, 3, 1), amount = amount, skip = 1)

    // Start year
    assertEquals(amount, yearly.valueOn(LocalDate(2020, 3, 1)))

    // Next year should not occur
    assertEquals(BigDecimal.ZERO, yearly.valueOn(LocalDate(2021, 3, 1)))

    // Two years later should occur
    assertEquals(amount, yearly.valueOn(LocalDate(2022, 3, 1)))
  }

  @Test
  fun `dates before start date do not produce value`() {
    val amount = BigDecimal.fromInt(20)
    val yearly = Yearly(name = "annual", startDate = LocalDate(2023, 5, 15), amount = amount)

    assertEquals(BigDecimal.ZERO, yearly.valueOn(LocalDate(2023, 5, 14)))
    assertEquals(amount, yearly.valueOn(LocalDate(2023, 5, 15)))
  }

  @Test
  fun `negative skip throws`() {
    assertFailsWith<IllegalArgumentException> {
      Yearly(name = "bad", startDate = LocalDate(2023, 1, 1), amount = BigDecimal.fromInt(1), skip = -1)
    }
  }

  @Test
  fun `feb 29 start occurs feb 28 on non-leap years`() {
    val amount = BigDecimal.fromInt(75)
    val yearly = Yearly(name = "leap", startDate = LocalDate(2020, 2, 29), amount = amount)

    // Leap year start
    assertEquals(amount, yearly.valueOn(LocalDate(2020, 2, 29)))

    // Non-leap year should occur on Feb 28
    assertEquals(amount, yearly.valueOn(LocalDate(2021, 2, 28)))

    // Not on Feb 27 or Mar 1
    assertEquals(BigDecimal.ZERO, yearly.valueOn(LocalDate(2021, 2, 27)))
    assertEquals(BigDecimal.ZERO, yearly.valueOn(LocalDate(2021, 3, 1)))

    // Next leap year occurrence
    assertEquals(amount, yearly.valueOn(LocalDate(2024, 2, 29)))
  }
}
