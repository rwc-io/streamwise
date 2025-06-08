package io.rwc.streamwise

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import kotlinx.datetime.LocalDate

data class DailyBalance(val date: LocalDate, val balance: BigDecimal)
