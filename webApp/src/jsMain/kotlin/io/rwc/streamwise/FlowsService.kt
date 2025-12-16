package io.rwc.streamwise

import io.rwc.streamwise.flows.*
import kangular.core.AngularWritable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@OptIn(ExperimentalJsExport::class)
@JsExport
class FlowsService(ngFlowBundlesSignal: dynamic, ngFlowsSignal: dynamic) {
  private val firestore = StreamFire.instance.firestore
  private val auth = StreamFire.instance.auth

  private var authWatcher: Job? = null

  private var flowBundlesSignal = AngularWritable<Array<FlowBundle>>(ngFlowBundlesSignal)
  private val flowsSignal = AngularWritable<Array<CashFlow>>(ngFlowsSignal)

  private var flowBundlesCollector: Job? = null
  private var flowsCollector: Job? = null

  init {
    // When auth changes, we need to re-query based on new auth
    val authFlow = auth.authStateChanged
    authWatcher = CoroutineScope(Dispatchers.Main).launch {
      authFlow.collect {
        removeAuthedListeners()

        if (it == null) {
          return@collect
        }

        val flowBundlesFlow =
          firestore.collection("flowBundles").where { "ownerUid" equalTo auth.currentUser?.uid }.snapshots

        flowBundlesCollector = CoroutineScope(Dispatchers.Main).launch {
          try {
            flowBundlesFlow.collect { snapshot ->
              val bundles = snapshot.documents.map { doc ->
                val bundle = doc.data(FlowBundle.serializer())
                bundle.copy(id = doc.id)
              }
              val bundleArray = bundles.toTypedArray()
              flowBundlesSignal.set(bundleArray)
              // Recompute flows whenever bundles change
              startFlowsCollector(bundleArray)
            }
          } finally {
            println("flowBundles collection flow completed")
          }
        }
      }
    }
  }

  private fun startFlowsCollector(bundles: Array<FlowBundle>) {
    flowsCollector?.cancel()
    if (bundles.isEmpty()) {
      flowsSignal.set(emptyArray())
      return
    }

    val db = StreamFire.instance.firestore

    val subCollectionFlows = listOf(
      bundles.readCashFlows(db, "fixedFlows", Fixed.serializer()),
      bundles.readCashFlows(db, "monthlyFlows", Monthly.serializer()),
      bundles.readCashFlows(db, "weeklyFlows", Weekly.serializer()),
      bundles.readCashFlows(db, "yearlyFlows", Yearly.serializer()),
    )
    val bundleFlows = combine(subCollectionFlows) { flowLists -> flowLists.flatMap { it } }

    flowsCollector = CoroutineScope(Dispatchers.Main).launch {
      bundleFlows.collectLatest { flows ->
        flowsSignal.set(flows.toTypedArray())
      }
    }
  }

  fun removeAuthedListeners() {
    flowBundlesCollector?.cancel()
    flowBundlesCollector = null

    flowsCollector?.cancel()
    flowsCollector = null
  }

  @Suppress("unused")
  fun ngOnDestroy() {
    removeAuthedListeners()
    authWatcher?.cancel()
  }
}

