package io.rwc.streamwise.flows

/**
 * Shared description for CashFlow types used by the UI.
 *
 * Throws IllegalArgumentException if the flow type is not recognized so callers
 * can handle and display a generic error message.
 */
fun CashFlow.describe(): String = when (this) {
  is Fixed -> describeFixed(this)
  is Monthly -> describeMonthly(this)
  is Weekly -> describeWeekly(this)
  is Yearly -> describeYearly(this)
}

fun describeFixed(flow: Fixed): String {
  return "Fixed on ${flow.date}: ${flow.amount.toStringExpanded()}"
}

fun describeMonthly(flow: Monthly): String {
  return "Monthly ${flow.name} on day offset ${flow.dayOffset}: ${flow.amount.toStringExpanded()}"
}

fun describeWeekly(flow: Weekly): String {
  return "Weekly ${flow.name} starting ${flow.startDate} (every ${flow.skip + 1} week${if (flow.skip == 0) "" else "s"}): ${flow.amount.toStringExpanded()}"
}

fun describeYearly(flow: Yearly): String {
  return "Yearly ${flow.name} starting ${flow.startDate} (every ${flow.skip + 1} year${if (flow.skip == 0) "" else "s"}): ${flow.amount.toStringExpanded()}"
}
