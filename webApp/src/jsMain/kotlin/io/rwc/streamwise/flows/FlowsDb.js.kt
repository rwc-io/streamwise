package io.rwc.streamwise.flows

import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlin.js.Promise
import io.rwc.streamwise.flows.saveFlow as sharedSaveFlow

@OptIn(ExperimentalCoroutinesApi::class)
object FlowsDbJs {
  fun saveFlow(db: FirebaseFirestore, cashFlow: CashFlow): Promise<Unit> = GlobalScope.promise {
    console.log("Saving flow: ${cashFlow.describe()}")
    sharedSaveFlow(db, cashFlow)
  }
}