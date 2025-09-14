package io.rwc.streamwise.flows

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class WeeklyTest {

  @Test
  fun `occurs on start date and every 7 days thereafter when skip is 0`() {
    val amount = BigDecimal.fromInt(50)
    val weekly = Weekly(name = "rent fragment", startDate = LocalDate(2023, 1, 1), amount = amount, skip = 0)

    // On start date
    assertEquals(amount, weekly.valueOn(LocalDate(2023, 1, 1)))

    // 7 days later
    assertEquals(amount, weekly.valueOn(LocalDate(2023, 1, 8)))

    // Not on an occurrence
    assertEquals(BigDecimal.ZERO, weekly.valueOn(LocalDate(2023, 1, 9)))
  }

  @Test
  fun `occurs every 2 weeks when skip is 1`() {
    val amount = BigDecimal.fromInt(75)
    val weekly = Weekly(name = "biweekly", startDate = LocalDate(2023, 1, 3), amount = amount, skip = 1)

    // Start
    assertEquals(amount, weekly.valueOn(LocalDate(2023, 1, 3)))

    // 14 days later
    assertEquals(amount, weekly.valueOn(LocalDate(2023, 1, 17)))

    // 7 days later (should not occur)
    assertEquals(BigDecimal.ZERO, weekly.valueOn(LocalDate(2023, 1, 10)))
  }

  @Test
  fun `dates before start date do not produce value`() {
    val amount = BigDecimal.fromInt(20)
    val weekly = Weekly(name = "weekly", startDate = LocalDate(2023, 5, 15), amount = amount)

    assertEquals(BigDecimal.ZERO, weekly.valueOn(LocalDate(2023, 5, 14)))
    assertEquals(amount, weekly.valueOn(LocalDate(2023, 5, 15)))
  }

  @Test
  fun `negative skip throws`() {
    assertFailsWith<IllegalArgumentException> {
      Weekly(name = "bad", startDate = LocalDate(2023, 1, 1), amount = BigDecimal.fromInt(1), skip = -1)
    }
  }
}
