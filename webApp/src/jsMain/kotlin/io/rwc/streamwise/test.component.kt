package io.rwc.streamwise

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import external.ChartData
import external.ChartDataset
import io.rwc.streamwise.flows.Fixed
import io.rwc.streamwise.flows.accumulateFlows
import kangular.core.Computed
import kangular.core.Signal
import kangular.external.AngularCore.effect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import kotlin.random.Random

@OptIn(ExperimentalJsExport::class)
@JsExport
class TestComponent {
  private val theNumber = BigDecimal.fromFloat(4.2f)

  private val aSignalK = Signal(theNumber)

  @Suppress("unused")
  val aSignal = aSignalK.ngSignal

  private val dataService = StreamFire.dataService

  @Suppress("unused")
  val flowBundles = dataService.flowBundles

  private val startDate = LocalDate(2025, 1, 1)
  private val endDate = LocalDate(2025, 12, 31)

  private val balances = Signal(
    listOf<Fixed>()
  )

  @Suppress("unused")
  val chartData = Computed {
    ChartData(
      labels = balances().map { it.date.toString() }.toTypedArray(),
      datasets = arrayOf(
        ChartDataset(
          label = "Balance",
          data = balances().map { it.amount.toStringExpanded() }.toTypedArray(),
          backgroundColor = "#4bc0c0ff",
          borderWidth = 1,
          fill = false,
          borderColor = "#4bc0c0ff"
        )
      )
    ).toJs()
  }

  @Suppress("unused")
  val barChartOptions = jsObjectOf(
    "scales" to jsObjectOf(
      "x" to timeAxisX,
      "y" to currencyAxisY,
    ),
    "plugins" to jsObjectOf(
      "legend" to jsObjectOf(
        "display" to true,
        "position" to "top"
      ),
      "tooltip" to currencyTooltip
    )
  )

  private var monthlysCollector: Job? = null

  @Suppress("unused")
  fun ngOnInit() {
    BigDecimal.useToStringExpanded = true
  }

  init {
    effect {
      val bundles = flowBundles()
      monthlysCollector?.cancel()
      monthlysCollector = null

      console.log("Recomputing balances")

      val db = StreamFire.dataService.db

      val allMonthlys = combine(bundles.map { bundle ->
        val doc = db.collection("flowBundles").document(bundle.id).collection("monthlyFlows")
        doc.snapshots
      }) { snapshotsArray ->
        snapshotsArray.flatMap { snapshot ->
          snapshot.documents.mapNotNull { doc ->
            try {
              doc.data(io.rwc.streamwise.flows.Monthly.serializer())
            } catch (e: Exception) {
              console.error("Error deserializing Monthly from doc ${doc.id}: $e. Cause: $e.cause")
              null
            }
          }
        }
      }

      monthlysCollector = CoroutineScope(Dispatchers.Main).launch {
        allMonthlys.collectLatest { monthlys ->
          balances.value = accumulateFlows(monthlys, startDate, endDate, 0.toBigDecimal())
        }
      }
    }
  }

  @Suppress("unused")
  fun ngOnDestroy() {
    monthlysCollector?.cancel()
    monthlysCollector = null
  }

  @Suppress("unused")
  fun increment() {
    val x = aSignalK()
    aSignalK.value = x + 1

    // Add a random balance entry to the end of the balances
    val oldBalances = balances()
    val lastDate = oldBalances.maxOfOrNull { it.date }
    val nextDate = lastDate?.plus(1, kotlinx.datetime.DateTimeUnit.DAY) ?: LocalDate(2025, 1, 4)
    val nextBalance = Random.nextFloat() * 2000
    balances.value = oldBalances + Fixed(
      nextDate,
      BigDecimal.fromFloat(nextBalance)
    )
  }
}