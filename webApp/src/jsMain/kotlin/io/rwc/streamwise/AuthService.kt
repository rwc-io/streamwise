package io.rwc.streamwise

import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.externals.getRedirectResult
import dev.gitlive.firebase.auth.js
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
    println("Listening for auth state changes...")
    authCollector = CoroutineScope(Dispatchers.Main).launch {
      authFlow.collect { newState ->
        if (newState == null) {
          println("User is signed out")
          currentAuth.value = null
        } else {
          println("${newState.uid} is signed in")
          currentAuth.value = newState
        }
      }
    }
  }

  fun cleanup() {
    authCollector?.cancel()
  }
}

@OptIn(ExperimentalJsExport::class)
@JsExport
fun checkAuthRedirectResult() {
  val auth = StreamFire.auth
  getRedirectResult(auth.js).then { result ->
    if (result != null) {
      println("Processed sign-in redirect result for user ${result.user.uid}")
    }
  }.catch { error ->
    println("Error during redirect sign-in: $error")
  }
}