package io.rwc.streamwise.flows

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

interface CashFlow {
  fun valueOn(date: LocalDate): BigDecimal
}

fun reifyFlows(flows: List<CashFlow>, startDate: LocalDate, endDate: LocalDate): List<Fixed> {
  val cashFlow = mutableListOf<Fixed>()

  var date = startDate
  while (date <= endDate) {
    val dayAmount = flows.fold(0.toBigDecimal()) { total, flow -> total + flow.valueOn(date) }
    cashFlow.add(Fixed(date, dayAmount))
    date = date.plus(1, DateTimeUnit.DAY)
  }

  return cashFlow
}

fun makeSomeBalances(startDate: LocalDate, endDate: LocalDate): List<Fixed> {
  val flows = listOf(
    Fixed(LocalDate(2023, 1, 1), 1000.toBigDecimal()),
    Monthly("ebmud", 5, (-100).toBigDecimal()),
    Monthly("pg&e", -5, (-200).toBigDecimal()),
    Monthly("income", 15, 1000.toBigDecimal()),
    Monthly("stuff", -7, (-500).toBigDecimal()),
    Monthly("income", 30, 1000.toBigDecimal()),
  )
  return accumulateFlows(flows, startDate, endDate)
}

fun accumulateFlows(flows: List<CashFlow>, startDate: LocalDate, endDate: LocalDate, startingBalance: BigDecimal = 0.toBigDecimal()): List<Fixed> {
  val cashFlow = reifyFlows(flows, startDate, endDate)
  var runningTotal = startingBalance
  return cashFlow.sortedBy { it.date }.map {
    runningTotal += it.amount
    Fixed(date = it.date,runningTotal)
  }
}