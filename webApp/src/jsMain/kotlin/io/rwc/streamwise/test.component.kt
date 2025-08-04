package io.rwc.streamwise

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import kangular.core.Signal

@OptIn(ExperimentalJsExport::class)
@JsExport
class TestComponent {
  private val theNumber  = BigDecimal.fromFloat(4.2f)

  private val aSignalK = Signal(theNumber)
  val aSignal = aSignalK.ngSignal

  fun ngOnInit() {
    println("TestComponent initialized with number: $theNumber")
  }

  fun increment() {
    val x = aSignalK()
    aSignalK.set(x + 1)
  }
}