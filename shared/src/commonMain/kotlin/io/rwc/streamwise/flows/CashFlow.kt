package io.rwc.streamwise.flows

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

interface CashFlow {
  fun valueOn(date: LocalDate): BigDecimal
}

data class Fixed(val date: LocalDate, val amount: BigDecimal) : CashFlow {
  override fun valueOn(date: LocalDate): BigDecimal {
    return if (this@Fixed.date == date) {
      amount
    } else {
      0.toBigDecimal()
    }
  }
}

fun reifyFlows(flows: List<CashFlow>, startDate: LocalDate, endDate: LocalDate): List<Fixed> {
  val cashFlow = mutableListOf<Fixed>()

  var date = startDate
  while (date <= endDate) {
    val dayAmount = flows.fold(0.toBigDecimal(), { total, flow -> total + flow.valueOn(date) })
    cashFlow.add(Fixed(date, dayAmount))
    date = date.plus(1, DateTimeUnit.DAY)
  }

  return cashFlow
}