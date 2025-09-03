package io.rwc.streamwise

import dev.gitlive.firebase.auth.externals.signInWithRedirect
import dev.gitlive.firebase.auth.js
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalJsExport::class)
@JsExport
class AuthComponent {
  private val auth = StreamFire.instance.auth

  @Suppress("unused")
  fun ngOnInit() {
  }

  @Suppress("unused")
  fun login() {
    val provider = dev.gitlive.firebase.auth.externals.GoogleAuthProvider()
    provider.addScope("email")

    signInWithRedirect(auth.js, provider)
  }

  @Suppress("unused")
  fun logout() {
    CoroutineScope(Dispatchers.Main).launch {
      auth.signOut()
    }
  }
}