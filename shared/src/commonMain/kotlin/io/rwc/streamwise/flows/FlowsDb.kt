package io.rwc.streamwise.flows

import dev.gitlive.firebase.firestore.FirebaseFirestore
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.serialization.KSerializer


private val logger = KotlinLogging.logger {}

suspend fun saveFlow(db: FirebaseFirestore, cashFlow: CashFlow) {
  val docRef = when (cashFlow) {
    is Fixed -> cashFlow.dbRef
    is Monthly -> cashFlow.dbRef
    is Weekly -> cashFlow.dbRef
    is Yearly -> cashFlow.dbRef
  }
  val ref = db.document(docRef)

  when (cashFlow) {
    is Fixed -> {
      ref.set(Fixed.serializer(), cashFlow)
    }

    is Monthly -> {
      ref.set(Monthly.serializer(), cashFlow)
    }

    is Weekly -> {
      ref.set(Weekly.serializer(), cashFlow)
    }

    is Yearly -> {
      ref.set(Yearly.serializer(), cashFlow)
    }
  }
}

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
