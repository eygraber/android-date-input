package com.eygraber.date_input.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import com.eygraber.date_input.common.DateResult
import com.eygraber.date_input.common.generateLocalizedMonthNames

enum class DateInputStyle {
  Filled,
  Outlined
}

@Composable
fun DateInput(
  dateInputHolder: DateInputHolder,
  error: (@Composable () -> Unit)?,
  modifier: Modifier = Modifier,
  style: DateInputStyle = DateInputStyle.Filled,
  horizontalSpacing: Dp = defaultHorizontalSpacing,
  verticalSpacing: Dp = defaultVerticalSpacing,
  monthDisplayNames: List<String> = generateLocalizedMonthNames(),
  monthPlaceholder: String? = null,
  monthLabel: @Composable () -> Unit = defaultMonthLabel,
  dayLabel: @Composable () -> Unit = defaultDayLabel,
  yearLabel: @Composable () -> Unit = defaultYearLabel,
  onDateChanged: (DateResult) -> Unit
) {
  AnnotatedStringDateInput(
    dateInputHolder = dateInputHolder,
    error = error,
    modifier = modifier,
    style = style,
    horizontalSpacing = horizontalSpacing,
    verticalSpacing = verticalSpacing,
    monthDisplayNames = monthDisplayNames.map(::AnnotatedString),
    monthPlaceholder = monthPlaceholder?.let(::AnnotatedString),
    monthLabel = monthLabel,
    dayLabel = dayLabel,
    yearLabel = yearLabel,
    onDateChanged = onDateChanged
  )
}

@Composable
fun AnnotatedStringDateInput(
  dateInputHolder: DateInputHolder,
  error: (@Composable () -> Unit)?,
  modifier: Modifier = Modifier,
  style: DateInputStyle = DateInputStyle.Filled,
  horizontalSpacing: Dp = defaultHorizontalSpacing,
  verticalSpacing: Dp = defaultVerticalSpacing,
  monthDisplayNames: List<AnnotatedString> = generateLocalizedMonthNames().map(::AnnotatedString),
  monthPlaceholder: AnnotatedString? = null,
  monthLabel: @Composable () -> Unit = defaultMonthLabel,
  dayLabel: @Composable () -> Unit = defaultDayLabel,
  yearLabel: @Composable () -> Unit = defaultYearLabel,
  onDateChanged: (DateResult) -> Unit
) {
  Column(modifier = modifier) {
    MonthDayAndYear(
      dateInputHolder = dateInputHolder,
      isError = error != null,
      onDateChanged = onDateChanged,
      style = style,
      horizontalSpacing = horizontalSpacing,
      verticalSpacing = verticalSpacing,
      monthDisplayNames = monthDisplayNames,
      monthPlaceholder = monthPlaceholder,
      monthLabel = monthLabel,
      dayLabel = dayLabel,
      yearLabel = yearLabel
    )

    error?.invoke()
  }
}
