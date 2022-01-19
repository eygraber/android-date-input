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
    var month by remember(dateInputHolder.initialDate?.monthValue) {
      mutableStateOf(dateInputHolder.initialDate?.monthValue)
    }

    var day by remember(dateInputHolder.initialDate?.dayOfMonth) {
      mutableStateOf(dateInputHolder.initialDate?.dayOfMonth)
    }

    var year by remember(dateInputHolder.initialDate?.year) {
      mutableStateOf(dateInputHolder.initialDate?.year)
    }

    fun notifyDateChanged(holder: DateInputHolder) {
      onDateChanged(
        DateResult.calculateResult(
          minDate = holder.minDate,
          maxDate = holder.maxDate,
          month = month,
          day = day,
          year = year
        )
      )
    }

    // calculate initial state
    LaunchedEffect(Unit) {
      notifyDateChanged(dateInputHolder)
    }

    Month(
      month = month,
      onMonthChange = { newMonth ->
        month = newMonth
        notifyDateChanged(dateInputHolder)
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
        notifyDateChanged(dateInputHolder)
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
        notifyDateChanged(dateInputHolder)
      },
      style = style,
      label = yearLabel,
      maxLength = 4,
      minWidth = 75.dp,
      isError = isError
    )
  }
}
