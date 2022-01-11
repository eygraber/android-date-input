package com.eygraber.date_input.common

import java.time.LocalDate

sealed interface DateResult {
  data class Success(val date: LocalDate) : DateResult
  data class Error(val error: Throwable) : DateResult

  data class ViolatedMinDate(val date: LocalDate, val minDate: LocalDate) : DateResult
  data class ViolatedMaxDate(val date: LocalDate, val maxDate: LocalDate) : DateResult

  object RequiresMonth : DateResult
  object RequiresDay : DateResult
  object RequiresYear : DateResult

  fun getOrNull() = when(this) {
    is Success -> date
    else -> null
  }

  companion object {
    fun calculateResult(
      minDate: LocalDate?,
      maxDate: LocalDate?,
      month: Int?,
      day: Int?,
      year: Int?
    ) = when {
      month == null -> RequiresMonth
      day == null -> RequiresDay
      year == null -> RequiresYear
      else -> runCatching {
        LocalDate.of(
          year, month, day
        )
      }.let { result ->
        when(val date = result.getOrNull()) {
          null -> Error(result.exceptionOrNull() ?: RuntimeException())
          else -> when {
            minDate != null && date < minDate -> ViolatedMinDate(date, minDate)
            maxDate != null && date > maxDate -> ViolatedMaxDate(date, maxDate)
            else -> Success(date)
          }
        }
      }
    }
  }
}
