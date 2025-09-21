package io.rwc.streamwise

import io.rwc.streamwise.flows.CashFlow
import io.rwc.streamwise.flows.Fixed
import io.rwc.streamwise.flows.accumulateFlows
import kangular.core.AngularWritable
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

@OptIn(ExperimentalJsExport::class)
@JsExport
class App {
  @Suppress("unused")
  fun realizeFlowsToBalances(ngBalancesSignal: dynamic, flows: Array<CashFlow>) {
    val balancesSignal = AngularWritable<Array<Fixed>>(ngBalancesSignal)
    val startDate = LocalDate(2025, 9, 13)
    val endDate = startDate.plus(DatePeriod(years = 2))
    val balances = accumulateFlows(flows.toList(), startDate, endDate)
    balancesSignal.set(balances.toTypedArray())
  }
}