package com.eygraber.date_time_input.compose

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eygraber.date_time_input.common.R
import java.time.LocalDate

@Immutable
data class DateInputHolder(
  val initialDate: LocalDate? = null,
  val minDate: LocalDate? = null,
  val maxDate: LocalDate? = null
)

internal val defaultHorizontalSpacing = 16.dp
internal val defaultVerticalSpacing = 8.dp

internal val defaultMonthLabel = @Composable {
  Text(text = stringResource(id = R.string.date_input_view_month_label))
}

internal val defaultDayLabel = @Composable {
  Text(text = stringResource(id = R.string.date_input_view_day_label))
}

internal val defaultYearLabel = @Composable {
  Text(text = stringResource(id = R.string.date_input_view_year_label))
}
