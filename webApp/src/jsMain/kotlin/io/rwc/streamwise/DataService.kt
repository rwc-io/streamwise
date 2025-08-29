package io.rwc.streamwise

import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kangular.core.Signal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.io.println

@Serializable
data class PublicData(
  val id: String,
  val content: String
)

class DataService {
  val auth: FirebaseAuth = StreamFire.auth
  var authCollector: kotlinx.coroutines.Job? = null

  val db: FirebaseFirestore = StreamFire.firestore
  var publicCollector: kotlinx.coroutines.Job? = null

  val currentAuth = Signal<FirebaseUser?>(null)

  init {
    val authFlow = auth.authStateChanged
    authCollector = CoroutineScope(Dispatchers.Main).launch {
      authFlow.collect { newState ->
        if (newState == null) {
          // User is signed out
          println("User is signed out")
          return@collect
        }
        println("Auth state changed: $newState")
        currentAuth.set(newState)
      }
    }

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
    authCollector?.cancel()
    publicCollector?.cancel()
  }
}