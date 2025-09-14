package io.rwc.streamwise.flows

/**
 * Shared description for CashFlow types used by the UI.
 *
 * Throws IllegalArgumentException if the flow type is not recognized so callers
 * can handle and display a generic error message.
 */
fun CashFlow.describe(): String = when (this) {
  is Fixed -> "Fixed on ${this.date}: ${this.amount}"
  is Monthly -> "Monthly ${this.name} on day offset ${this.dayOffset}: ${this.amount}"
  is Weekly -> "Weekly ${this.name} starting ${this.startDate} (every ${this.skip + 1} week${if (this.skip == 0) "" else "s"}): ${this.amount}"
  is Yearly -> "Yearly ${this.name} starting ${this.startDate} (every ${this.skip + 1} year${if (this.skip == 0) "" else "s"}): ${this.amount}"
}
