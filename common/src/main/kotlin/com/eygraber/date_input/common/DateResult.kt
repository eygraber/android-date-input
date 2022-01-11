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
}
