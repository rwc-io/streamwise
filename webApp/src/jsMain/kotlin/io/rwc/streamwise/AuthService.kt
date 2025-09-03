package io.rwc.streamwise

import dev.gitlive.firebase.auth.externals.User
import dev.gitlive.firebase.auth.externals.getRedirectResult
import dev.gitlive.firebase.auth.js
import kangular.core.AngularWritable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalJsExport::class)
@JsExport
class AuthService(currentUserNgSignal: dynamic) {
  private val auth = StreamFire.instance.auth
  private var authCollector: kotlinx.coroutines.Job? = null

  private val currentAuth = AngularWritable<User?>(ngSignal = currentUserNgSignal)

  init {
    val authFlow = auth.authStateChanged
    println("Listening for auth state changes...")
    authCollector = CoroutineScope(Dispatchers.Main).launch {
      try {
        authFlow.collect { newState ->
          if (newState == null) {
            println("User is signed out")
            currentAuth.set(null)
          } else {
            println("${newState.uid} is signed in")
            currentAuth.set(newState.js)
          }
        }
      } finally {
        println("Auth state change flow completed")
      }
    }
  }

  fun ngOnDestroy() {
    authCollector?.cancel()
  }
}

@OptIn(ExperimentalJsExport::class)
@JsExport
fun checkAuthRedirectResult() {
  val auth = StreamFire.instance.auth
  getRedirectResult(auth.js).then { result ->
    if (result != null) {
      println("Processed sign-in redirect result for user ${result.user.uid}")
    }
  }.catch { error ->
    println("Error during redirect sign-in: $error")
  }
}