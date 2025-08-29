package io.rwc.streamwise

import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseUser
import kangular.core.Signal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthService {
  val auth: FirebaseAuth = StreamFire.auth
  var authCollector: kotlinx.coroutines.Job? = null

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
  }

  fun cleanup() {
    authCollector?.cancel()
  }
}
