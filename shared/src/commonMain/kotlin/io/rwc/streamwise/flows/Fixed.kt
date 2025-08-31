package io.rwc.streamwise.flows

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import com.ionspin.kotlin.bignum.serialization.kotlinx.bigdecimal.BigDecimalArraySerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Fixed(
  val date: LocalDate,
  @Serializable(with = BigDecimalArraySerializer::class)
  val amount: BigDecimal
) : CashFlow {
  override fun valueOn(date: LocalDate): BigDecimal {
    return if (this@Fixed.date == date) {
      amount
    } else {
      0.toBigDecimal()
    }
  }
}