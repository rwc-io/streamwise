package io.rwc.streamwise

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.auth.externals.connectAuthEmulator
import dev.gitlive.firebase.auth.js
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.externals.connectFirestoreEmulator
import dev.gitlive.firebase.firestore.externals.initializeFirestore
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.js
import dev.gitlive.firebase.initialize
import dev.gitlive.firebase.js

class StreamFire {
  companion object Singleton {
    private var streamfireInstance: StreamFire? = null

    val instance: StreamFire
      get() {
        if (streamfireInstance == null) {
          println("Instantiating StreamFire")
          streamfireInstance = StreamFire()
        }
        return streamfireInstance!!
      }

    init {
      // Configuration. Consider moving config to initializers elsewhere.
      initializeBigDecimal()
    }
  }

  val firestore: FirebaseFirestore
  val auth: FirebaseAuth

  init {
    val opts = js("Object.entries(require('./firebase-config.json'))").unsafeCast<Array<Array<dynamic>>>()
      .associate { (key, value) -> key.toString() to value.toString() }
    val useEmulators = opts["useEmulators"].toBoolean()
    val options = FirebaseOptions(
      projectId = opts["projectId"] ?: "unknown-project-id",
      applicationId = opts["appId"] ?: "unknown-app-id",
      apiKey = opts["apiKey"] ?: "unknown-api-key",
      authDomain = opts["authDomain"] ?: "unknown-auth-domain",
      storageBucket = opts["storageBucket"] ?: "unknown-storage-bucket",
      gcmSenderId = opts["messagingSenderId"] ?: "unknown-messaging-sender",
    )
    val app = Firebase.initialize(options = options)
    auth = Firebase.auth(app)
    initializeFirestore(app.js, {})
    firestore = Firebase.firestore(app)

    if (useEmulators) {
      console.log("Using emulators")
      connectAuthEmulator(auth.js, "http://localhost:9099")
      connectFirestoreEmulator(firestore.js, "localhost", 8080)
    }
  }
}
