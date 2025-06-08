package io.rwc.streamwise

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.Symbol
import io.github.koalaplot.core.legend.LegendLocation
import io.github.koalaplot.core.line.LinePlot
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.util.VerticalRotation
import io.github.koalaplot.core.util.rotateVertically
import io.github.koalaplot.core.xygraph.*
import kotlinx.datetime.LocalDate
import kotlin.time.ExperimentalTime

internal val padding = 8.dp
internal val paddingMod = Modifier.padding(padding)

@Composable
fun ChartTitle(title: String) {
  Column {
    Text(
      title,
      color = MaterialTheme.colorScheme.onBackground,
      style = MaterialTheme.typography.titleLarge,
      modifier = Modifier.align(Alignment.CenterHorizontally)
    )
  }
}

@Composable
fun AxisLabel(label: String, modifier: Modifier = Modifier) {
  Text(
    label,
    color = MaterialTheme.colorScheme.onBackground,
    style = MaterialTheme.typography.bodySmall,
    modifier = modifier,
    overflow = TextOverflow.Ellipsis,
    maxLines = 1
  )
}

@Composable
fun AxisTitle(title: String, modifier: Modifier = Modifier) {
  Text(
    title,
    color = MaterialTheme.colorScheme.onBackground,
    style = MaterialTheme.typography.titleMedium,
    modifier = modifier
  )
}

@OptIn(ExperimentalKoalaPlotApi::class, ExperimentalTime::class)
@Composable
@Suppress("MagicNumber")
fun TimeSamplePlot(balances: List<DailyBalance>, thumbnail: Boolean, title: String) {
  val data = balances.map { DefaultPoint(it.date, it.balance) }
  val yDataMin = data.minOfOrNull { it.y } ?: 0.toBigDecimal()
  val yDataMax = data.maxOfOrNull { it.y } ?: 1.toBigDecimal()

  Column {
    ChartLayout(
      modifier = paddingMod.padding(end = 16.dp),
      title = { ChartTitle(title) },
      legendLocation = LegendLocation.BOTTOM
    ) {
      XYGraph(
        xAxisModel = IntLinearAxisModel(
          range = (data.first().x.toEpochDays())..(data.last().x.toEpochDays()) + 1
        ),
        yAxisModel = DoubleLinearAxisModel(
          range = yDataMin.doubleValue(false) ..yDataMax.doubleValue(false)
        ),
        xAxisLabels = {
          if (!thumbnail) {
            val date = LocalDate.fromEpochDays(it)
            val dateStr = date.toString()
            AxisLabel(dateStr, Modifier.padding(top = 2.dp))
          }
        },
        xAxisStyle = rememberAxisStyle(labelRotation = 90),
        xAxisTitle = {
          if (!thumbnail) {
            Box(
              modifier = Modifier.fillMaxWidth(),
              contentAlignment = Alignment.Center
            ) {
              AxisTitle("Time")
            }
          }
        },
        yAxisLabels = {
          if (!thumbnail) AxisLabel(it.toString(), Modifier.absolutePadding(right = 2.dp))
        },
        yAxisTitle = {
          if (!thumbnail) {
            Box(
              modifier = Modifier.fillMaxHeight(),
              contentAlignment = Alignment.TopStart
            ) {
              AxisTitle(
                "Value",
                modifier = Modifier.rotateVertically(VerticalRotation.COUNTER_CLOCKWISE)
                  .padding(bottom = padding)
              )
            }
          }
        }
      ) {
        Chart(data, thumbnail)
      }
    }
  }
}

@Composable
fun HoverSurface(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
  Surface(
    shadowElevation = 2.dp,
    shape = MaterialTheme.shapes.medium,
    color = Color.LightGray,
    modifier = modifier.padding(padding)
  ) {
    Box(modifier = Modifier.padding(padding)) {
      content()
    }
  }
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
private fun XYGraphScope<Int, Double>.Chart(data: List<DefaultPoint<LocalDate, BigDecimal>>, thumbnail: Boolean) {
  val chartData = data.map { point ->
    DefaultPoint(
      x = point.x.toEpochDays(),
      y = point.y.doubleValue(false)
    )
  }
  LinePlot(
    data = chartData,
    lineStyle = LineStyle(
      brush = SolidColor(Color.Black),
      strokeWidth = 2.dp
    ),
    symbol = { point ->
      Symbol(
        shape = CircleShape,
        fillBrush = SolidColor(Color.Black),
        modifier = Modifier.then(
          if (!thumbnail) {
            Modifier.hoverableElement {
              HoverSurface { Text(point.y.toString()) }
            }
          } else {
            Modifier
          }
        )
      )
    }
  )
}
