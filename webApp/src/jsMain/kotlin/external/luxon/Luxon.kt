package external.luxon

import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

@JsName("luxon.DateTime")
external class DateTime {
  companion object {
    fun fromObject(obj: dynamic): DateTime
    fun fromISO(isoString: String): DateTime
    fun local(year: Int, month: Int, day: Int): DateTime
  }

  val year: Int
  val month: Int
  val day: Int
}

fun DateTime.toKotlin(): LocalDate {
  return LocalDate(year, month, day)
}

fun LocalDate.toLuxon(): DateTime {
  return DateTime.local(this.year, this.month.number, this.day)
}