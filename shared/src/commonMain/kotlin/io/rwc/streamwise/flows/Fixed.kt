package io.rwc.streamwise.flows

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import com.ionspin.kotlin.bignum.serialization.kotlinx.bigdecimal.BigDecimalHumanReadableSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Fixed(
  val date: LocalDate,
  @Serializable(with = BigDecimalHumanReadableSerializer::class)
  val amount: BigDecimal,
  @Transient val dbRef: String = "",
) : CashFlow {
  override fun valueOn(date: LocalDate): BigDecimal {
    return if (this@Fixed.date == date) {
      amount
    } else {
      0.toBigDecimal()
    }
  }
}