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

@Serializable
data class PrivateData(
  val id: String,
  val content: String,
)

class DataService {
  val db: FirebaseFirestore = StreamFire.firestore
  var publicCollector: kotlinx.coroutines.Job? = null
  var privateCollector: kotlinx.coroutines.Job? = null
  var authWatcher: kotlinx.coroutines.Job? = null

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

    // When auth changes, we need to re-query based on new auth
    val authFlow = StreamFire.auth.authStateChanged
    authWatcher = CoroutineScope(Dispatchers.Main).launch {
      authFlow.collect {
        removeAuthedListeners()

        if (it == null) {
          return@collect
        }

        val privateFlow =
          db.collection("private").where { "ownerUid" equalTo StreamFire.auth.currentUser?.uid }.snapshots

        privateCollector = CoroutineScope(Dispatchers.Main).launch {
          try {
            privateFlow.collect { snapshot ->
              println("Private collection changed: ${snapshot.documents.size} documents")
              for (doc in snapshot.documents) {
                println("Private doc: ${doc.id} => ${doc.data(PrivateData.serializer())}")
              }
            }
          } finally {
            println("Private collection flow completed")
          }
        }
      }
    }
  }

  fun removeAuthedListeners() {
    privateCollector?.cancel()
    privateCollector = null
  }

  fun cleanup() {
    removeAuthedListeners()
    publicCollector?.cancel()
    authWatcher?.cancel()
  }
}