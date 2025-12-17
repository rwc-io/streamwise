@file:JsModule("luxon")
@file:JsNonModule
package external.luxon

@JsName("DateTime")
external class DateTime {
  companion object {
    fun fromISO(isoString: String): DateTime
  }

  fun toISODate(): String
}