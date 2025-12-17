package io.rwc.streamwise

import com.ionspin.kotlin.bignum.decimal.BigDecimal

interface Platform {
  val name: String
}

expect fun getPlatform(): Platform

// BigDecimal serialization should use the expanded format, not scientific notation
fun initializeBigDecimal() {
  BigDecimal.useToStringExpanded = true
}