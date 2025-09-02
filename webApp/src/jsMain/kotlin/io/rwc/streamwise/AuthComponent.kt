package io.rwc.streamwise

import dev.gitlive.firebase.auth.externals.signInWithRedirect
import dev.gitlive.firebase.auth.js
import kangular.external.AngularCore.computed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalJsExport::class)
@JsExport
class AuthComponent {
  private val auth = StreamFire.instance.auth

  @Suppress("unused")
  val authedUser = computed {
    // Getting the currentAuth directly does not seem to work.
    // When accessed from JS, it contains an extra object layer,
    // which I think is the js object field. JS doesn't seem to
    // see the properties.
    StreamFire.authService.currentAuth()?.js
  }

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