package io.rwc.streamwise

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.auth.externals.connectAuthEmulator
import dev.gitlive.firebase.auth.js
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.externals.connectFirestoreEmulator
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.js
import dev.gitlive.firebase.initialize

class StreamFire {
  companion object Companion {
    val firestore: FirebaseFirestore
    val auth: FirebaseAuth

    val authService: AuthService
    val dataService: DataService

    init {
      val useEmulators = true

      val opts = js("Object.entries(require('./firebase-config.json'))").unsafeCast<Array<Array<dynamic>>>()
        .associate { (key, value) -> key.toString() to value.toString() }
      val options = FirebaseOptions(
        applicationId = opts["applicationId"] ?: "unknown-app-id",
        projectId = opts["projectId"] ?: "unknown-project-id",
        apiKey = opts["apiKey"] ?: "unknown-api-key",
        authDomain = opts["authDomain"] ?: "unknown-auth-domain",
      )
      Firebase.initialize(options = options)

      firestore = Firebase.firestore
      auth = Firebase.auth

      if (useEmulators) {
        connectAuthEmulator(auth.js, "http://localhost:9099")
        connectFirestoreEmulator(firestore.js, "localhost", 8080)
      }

      authService = AuthService()
      dataService = DataService()

      /*CoroutineScope(Dispatchers.Main).launch {
        auth.signInWithEmailAndPassword("user@example.com", "password")
      }*/
    }
  }
}