package com.eygraber.date_input.compose

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eygraber.date_input.common.R
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
  Text(text = stringResource(id = R.string.date_input_view_month_hint))
}

internal val defaultDayLabel = @Composable {
  Text(text = stringResource(id = R.string.date_input_view_day_hint))
}

internal val defaultYearLabel = @Composable {
  Text(text = stringResource(id = R.string.date_input_view_year_hint))
}
