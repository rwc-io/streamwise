package io.rwc.streamwise

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import io.rwc.streamwise.flows.Fixed
import io.rwc.streamwise.flows.Monthly
import io.rwc.streamwise.flows.reifyFlows
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
  val flows = listOf(
    Fixed(LocalDate(2023, 1, 1), 1000.toBigDecimal()),
    Monthly("ebmud", 5, -100.toBigDecimal()),
    Monthly("pg&e", -5, -200.toBigDecimal()),
    Monthly("income", 15, 1000.toBigDecimal()),
    Monthly("stuff", -7, -500.toBigDecimal()),
    Monthly("income", 30, 1000.toBigDecimal()),
  )
  val cash = reifyFlows(flows, LocalDate(2023, 1, 1), LocalDate(2023, 12, 31))
  var runningTotal = 0.toBigDecimal()
  val balances = cash.associate {
    runningTotal += it.amount
    it.date to runningTotal
  }

  MaterialTheme {
    Column(
      modifier = Modifier
        .safeContentPadding()
        .fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      TimeSamplePlot(balances, false, "Cash flow")
    }
  }
}
