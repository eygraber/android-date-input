package com.eygraber.date_input.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.eygraber.date_input.common.DateResult
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import java.time.LocalDate

@Composable
internal fun MonthDayAndYear(
  dateInputHolder: DateInputHolder,
  isError: Boolean,
  onDateChanged: (DateResult) -> Unit,
  style: DateInputStyle,
  horizontalSpacing: Dp,
  verticalSpacing: Dp,
  monthDisplayNames: List<AnnotatedString>,
  monthPlaceholder: AnnotatedString?,
  monthLabel: @Composable () -> Unit,
  dayLabel: @Composable () -> Unit,
  yearLabel: @Composable () -> Unit
) {
  FlowRow(
    mainAxisSpacing = horizontalSpacing,
    crossAxisSpacing = verticalSpacing,
    mainAxisAlignment = FlowMainAxisAlignment.Start,
    modifier = Modifier.fillMaxWidth()
  ) {
    var month by remember {
      mutableStateOf(dateInputHolder.initialDate?.monthValue)
    }

    var day by remember {
      mutableStateOf(dateInputHolder.initialDate?.dayOfMonth)
    }

    var year by remember {
      mutableStateOf(dateInputHolder.initialDate?.year)
    }

    fun notifyDateChanged(dateResult: DateResult) {
      onDateChanged(dateResult)
    }

    // calculate initial state
    LaunchedEffect(Unit) {
      notifyDateChanged(calculateDateResult(dateInputHolder, month, day, year))
    }

    Month(
      month = month,
      onMonthChange = { newMonth ->
        month = newMonth
        notifyDateChanged(calculateDateResult(dateInputHolder, month, day, year))
      },
      style = style,
      monthDisplayNames = monthDisplayNames,
      placeholder = monthPlaceholder,
      label = monthLabel,
      isError = isError
    )

    DateInputText(
      value = day,
      onValueChange = { newDay ->
        day = newDay
        notifyDateChanged(calculateDateResult(dateInputHolder, month, day, year))
      },
      style = style,
      label = dayLabel,
      maxLength = 2,
      minWidth = 68.dp,
      isError = isError
    )

    DateInputText(
      value = year,
      onValueChange = { newYear ->
        year = newYear
        notifyDateChanged(calculateDateResult(dateInputHolder, month, day, year))
      },
      style = style,
      label = yearLabel,
      maxLength = 4,
      minWidth = 75.dp,
      isError = isError
    )
  }
}

private fun calculateDateResult(
  holder: DateInputHolder,
  month: Int?,
  day: Int?,
  year: Int?
) = when {
  month == null -> DateResult.RequiresMonth
  day == null -> DateResult.RequiresDay
  year == null -> DateResult.RequiresYear
  else -> runCatching {
    LocalDate.of(
      year, month, day
    )
  }.let { result ->
    when(val date = result.getOrNull()) {
      null -> DateResult.Error(result.exceptionOrNull() ?: RuntimeException())
      else -> {
        val minDate = holder.minDate
        val maxDate = holder.maxDate
        when {
          minDate != null && date < minDate -> DateResult.ViolatedMinDate(date, minDate)
          maxDate != null && date > maxDate -> DateResult.ViolatedMaxDate(date, maxDate)
          else -> DateResult.Success(date)
        }
      }
    }
  }
}
