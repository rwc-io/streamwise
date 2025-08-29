package io.rwc.streamwise

import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class PublicData(
  val id: String,
  val content: String,
)

class DataService {
  val db: FirebaseFirestore = StreamFire.firestore
  var publicCollector: kotlinx.coroutines.Job? = null

  init {
    val publicFlow = db.collection("public").snapshots
    publicCollector = CoroutineScope(Dispatchers.Main).launch {
      try {
        publicFlow.collect { snapshot ->
          println("Public collection changed: ${snapshot.documents.size} documents")
          for (doc in snapshot.documents) {
            println("Document: ${doc.id} => ${doc.data(PublicData.serializer())}")
          }
        }
      } finally {
        println("Public collection flow completed")
      }
    }
  }

  fun cleanup() {
    publicCollector?.cancel()
  }
}