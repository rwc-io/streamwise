package external

@JsModule("luxon")
external object Luxon {
  object DateTime {
    fun local(year: Int, month: Int, day: Int): dynamic
    fun local(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): dynamic
  }
}
