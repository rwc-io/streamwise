package io.rwc.streamwise

import io.rwc.streamwise.flows.Fixed
import io.rwc.streamwise.flows.Monthly
import io.rwc.streamwise.flows.accumulateFlows
import kangular.core.Signal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class FlowBundleService {
  private var monthlysCollector: Job? = null

  fun start(
    targetBalances: Signal<List<Fixed>>,
    bundlesSignal: Signal<Array<io.rwc.streamwise.flows.FlowBundle>>,
    startDate: LocalDate,
    endDate: LocalDate,
  ) {
    stop()

    val bundles = bundlesSignal()
    val db = StreamFire.dataService.db

    // Build the combined flow of all bundles' monthlyFlows subcollections
    val allMonthlys = combine(bundles.map { bundle ->
      val doc = db.collection("flowBundles").document(bundle.id).collection("monthlyFlows")
      doc.snapshots
    }) { snapshotsArray ->
      snapshotsArray.flatMap { snapshot ->
        snapshot.documents.mapNotNull { doc ->
          try {
            doc.data(Monthly.serializer())
          } catch (e: Exception) {
            console.error("Error deserializing Monthly from doc ${doc.id}: $e. Cause: ${e.cause}")
            null
          }
        }
      }
    }

    val allFixed = combine(bundles.map { bundle ->
      val doc = db.collection("flowBundles").document(bundle.id).collection("fixedFlows")
      doc.snapshots
    }) { snapshotsArray ->
      snapshotsArray.flatMap { snapshot ->
        snapshot.documents.mapNotNull { doc ->
          try {
            doc.data(Fixed.serializer())
          } catch (e: Exception) {
            console.error("Error deserializing Fixed from doc ${doc.id}: $e. Cause: ${e.cause}")
            null
          }
        }
      }
    }

    val allFlows = combine(allMonthlys, allFixed) { monthlys, fixeds ->
      monthlys + fixeds
    }

    // Track the collector so it can be canceled/restarted as necessary
    monthlysCollector = CoroutineScope(Dispatchers.Main).launch {
      allFlows.collectLatest { flows ->
        targetBalances.value = accumulateFlows(flows, startDate, endDate)
      }
    }
  }

  fun stop() {
    monthlysCollector?.cancel()
    monthlysCollector = null
  }
}