package io.rwc.streamwise

import io.rwc.streamwise.flows.*
import kangular.core.WritableSignal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.serialization.KSerializer

class FlowBundleService {
  private var monthlysCollector: Job? = null

  fun start(
    targetBalances: WritableSignal<Array<Fixed>>,
    bundles: Array<FlowBundle>,
    startDate: LocalDate,
    endDate: LocalDate,
  ) {
    stop()

    val subCollectionFlows = listOf(
      bundles.readCashFlows("fixedFlows", Fixed.serializer()),
      bundles.readCashFlows("monthlyFlows", Monthly.serializer()),
    )
    val allFlows = combine(subCollectionFlows) { flowLists -> flowLists.flatMap { it } }

    monthlysCollector = CoroutineScope(Dispatchers.Main).launch {
      allFlows.collectLatest { flows ->
        targetBalances.set(accumulateFlows(flows, startDate, endDate).toTypedArray())
      }
    }
  }

  fun stop() {
    monthlysCollector?.cancel()
    monthlysCollector = null
  }
}

fun <T : CashFlow> Array<FlowBundle>.readCashFlows(type: String, serializer: KSerializer<T>): Flow<List<CashFlow>> {
  val db = StreamFire.instance.firestore

  return combine(this.map { bundle ->
    val doc = db.collection("flowBundles").document(bundle.id).collection(type)
    doc.snapshots
  }) { snapshotsArray ->
    snapshotsArray.flatMap { snapshot ->
      snapshot.documents.mapNotNull { doc ->
        try {
          doc.data(serializer)
        } catch (e: Exception) {
          console.error("Error deserializing Fixed from doc ${doc.id}: $e. Cause: ${e.cause}")
          null
        }
      }
    }
  }
}
