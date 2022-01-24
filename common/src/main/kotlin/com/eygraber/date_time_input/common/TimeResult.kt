package com.eygraber.date_time_input.common

import java.time.LocalTime

sealed interface TimeResult {
  data class Success(val time: LocalTime) : TimeResult
  data class Error(val error: Throwable) : TimeResult

  data class ViolatedMinTime(val time: LocalTime, val minTime: LocalTime) : TimeResult
  data class ViolatedMaxTime(val time: LocalTime, val maxTime: LocalTime) : TimeResult

  object RequiresHour : TimeResult
  object RequiresMinute : TimeResult
  object RequiresSecond : TimeResult

  fun getOrNull() = when(this) {
    is Success -> time
    else -> null
  }

  companion object {
    fun calculateResult(
      minTime: LocalTime?,
      maxTime: LocalTime?,
      hour: Int?,
      minute: Int?,
      second: Int? = 0
    ) = when {
      hour == null -> RequiresHour
      minute == null -> RequiresMinute
      second == null -> RequiresSecond
      else -> runCatching {
        LocalTime.of(
          hour, minute, second
        )
      }.let { result ->
        when(val time = result.getOrNull()) {
          null -> Error(result.exceptionOrNull() ?: RuntimeException())
          else -> when {
            minTime != null && time < minTime -> ViolatedMinTime(time, minTime)
            maxTime != null && time > maxTime -> ViolatedMaxTime(time, maxTime)
            else -> Success(time)
          }
        }
      }
    }
  }
}
