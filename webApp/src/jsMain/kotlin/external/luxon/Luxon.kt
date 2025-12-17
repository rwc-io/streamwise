package external.luxon

import kotlinx.datetime.LocalDate

fun DateTime.toLocalDate(): LocalDate = LocalDate.parse(this.toISODate())
