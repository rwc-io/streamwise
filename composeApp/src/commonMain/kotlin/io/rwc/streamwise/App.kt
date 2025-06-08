package io.rwc.streamwise

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
  val balances = listOf(
    DailyBalance(LocalDate(2023, 1, 1), 1000.toBigDecimal()),
    DailyBalance(LocalDate(2023, 1, 2), 1100.toBigDecimal()),
    DailyBalance(LocalDate(2023, 1, 3), 1050.toBigDecimal()),
    DailyBalance(LocalDate(2023, 1, 4), 1200.toBigDecimal()),
    DailyBalance(LocalDate(2023, 1, 5), 1150.toBigDecimal()),
    DailyBalance(LocalDate(2023, 1, 6), 1300.toBigDecimal()),
    DailyBalance(LocalDate(2023, 1, 7), 1250.toBigDecimal()),
    DailyBalance(LocalDate(2023, 1, 8), 1400.toBigDecimal()),
    DailyBalance(LocalDate(2023, 1, 9), 1350.toBigDecimal()),
    DailyBalance(LocalDate(2023, 1, 10), 1500.toBigDecimal()),
  )

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
