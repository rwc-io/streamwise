package io.rwc.streamwise

import io.rwc.streamwise.flows.FlowBundle
import kangular.core.WritableSignal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FlowsService() {
  private val firestore = StreamFire.instance.firestore
  private val auth = StreamFire.instance.auth

  private var authWatcher: kotlinx.coroutines.Job? = null

  var flowBundles = WritableSignal<Array<FlowBundle>>(emptyArray())
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
              flowBundles.value = bundles.toTypedArray()
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

  fun cleanup() {
    removeAuthedListeners()
    authWatcher?.cancel()
  }
}