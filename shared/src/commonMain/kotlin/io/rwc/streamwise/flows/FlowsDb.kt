package io.rwc.streamwise.flows

import dev.gitlive.firebase.firestore.FirebaseFirestore
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer


private val logger = KotlinLogging.logger {}

fun <T : CashFlow> Array<FlowBundle>.readCashFlows(
  db: FirebaseFirestore,
  type: String,
  serializer: KSerializer<T>,
): Flow<List<CashFlow>> {

  return combine(this.map { bundle ->
    val doc = db.collection("flowBundles").document(bundle.id).collection(type)
    doc.snapshots
  }) { snapshotsArray ->
    snapshotsArray.flatMap { snapshot ->
      snapshot.documents.mapNotNull { doc ->
        try {
          when (val flow = doc.data(serializer)) {
            is Fixed -> flow.copy(dbRef = doc.reference.path)
            is Monthly -> flow.copy(dbRef = doc.reference.path)
            is Weekly -> flow.copy(dbRef = doc.reference.path)
            is Yearly -> flow.copy(dbRef = doc.reference.path)
          }
        } catch (e: Exception) {
          logger.error { "Error deserializing flow from doc ${doc.id}: $e. Cause: ${e.cause}" }
          null
        }
      }
    }
  }
}
