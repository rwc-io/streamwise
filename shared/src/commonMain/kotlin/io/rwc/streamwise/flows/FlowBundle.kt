package io.rwc.streamwise.flows

import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

/**
 * A Bundle document is the root node for cash flow collections.
 * Cash flows are stored as subcollections (must be queried).
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class FlowBundle(
  val id: String,
  val name: String,
  val ownerUid: String,
)
