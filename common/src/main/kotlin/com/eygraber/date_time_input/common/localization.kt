package com.eygraber.date_time_input.common

import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

fun generateLocalizedMonthNames() =
  (1..12).map { month ->
    Month
      .of(month)
      .getDisplayName(
        TextStyle.FULL,
        Locale.getDefault()
      )
  }
