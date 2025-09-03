package io.rwc.streamwise

import io.rwc.streamwise.flows.FlowBundle
import kangular.core.AngularWritable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalJsExport::class)
@JsExport
class FlowsService(ngFlowBundlesSignal: dynamic) {
  private val firestore = StreamFire.instance.firestore
  private val auth = StreamFire.instance.auth

  private var authWatcher: kotlinx.coroutines.Job? = null

  private var flowBundlesSignal = AngularWritable<Array<FlowBundle>>(ngFlowBundlesSignal)
  private var flowBundlesCollector: kotlinx.coroutines.Job? = null

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
              flowBundlesSignal.set(bundles.toTypedArray())
            }
          } finally {
            println("flowBundles collection flow completed")
          }
        }
      }
    }
  }

  fun removeAuthedListeners() {
    flowBundlesCollector?.cancel()
    flowBundlesCollector = null
  }

  @Suppress("unused")
  fun ngOnDestroy() {
    removeAuthedListeners()
    authWatcher?.cancel()
  }
}