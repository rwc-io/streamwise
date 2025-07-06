package io.rwc.streamwise

import com.ionspin.kotlin.bignum.decimal.BigDecimal


class Test {
  private val num = BigDecimal.fromFloat(2.34f)

  init {
    println("Test class initialized with number: $num")
  }

  fun num(): BigDecimal {
    return num
  }
}

@OptIn(ExperimentalJsExport::class)
@JsExport
fun testFunction(): String {
  val x = Test()
  return x.num().toString()
}