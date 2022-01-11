@file:OptIn(ExperimentalMaterialApi::class)

package com.eygraber.date_input.compose

import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuBoxScope
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.eygraber.date_input.common.R

@Composable
internal fun Month(
  month: Int?,
  onMonthChange: (Int) -> Unit,
  style: DateInputStyle,
  monthDisplayNames: List<AnnotatedString>,
  placeholder: AnnotatedString?,
  label: @Composable () -> Unit,
  isError: Boolean
) {
  val isMonthsExpanded = remember { mutableStateOf(false) }

  val actualPlaceholder = placeholder ?: AnnotatedString(
    stringResource(id = R.string.date_input_view_month_placeholder)
  )

  val monthText = when(month) {
    null -> actualPlaceholder
    else -> monthDisplayNames[month - 1] // month is 1 based
  }

  ExposedDropdownMenuBox(
    expanded = isMonthsExpanded.value,
    onExpandedChange = {
      isMonthsExpanded.value = !isMonthsExpanded.value
    }
  ) {
    MonthTextField(
      style = style,
      isMonthsExpanded = isMonthsExpanded.value,
      selectedMonthText = monthText,
      placeholder = actualPlaceholder,
      label = label,
      isError = isError
    )

    MonthDropdownMenu(
      isMonthsExpanded = isMonthsExpanded,
      monthDisplayNames = monthDisplayNames,
      onMonthChange = onMonthChange
    )
  }
}

@Composable
private fun MonthTextField(
  style: DateInputStyle,
  isMonthsExpanded: Boolean,
  selectedMonthText: AnnotatedString,
  placeholder: AnnotatedString,
  label: @Composable () -> Unit,
  isError: Boolean
) {
  val trailingIcon = @Composable {
    ExposedDropdownMenuDefaults.TrailingIcon(
      expanded = isMonthsExpanded
    )
  }

  val modifier = Modifier.widthIn(168.dp)

  when(style) {
    DateInputStyle.Filled -> TextField(
      readOnly = true,
      value = TextFieldValue(selectedMonthText),
      onValueChange = {},
      modifier = modifier,
      label = label,
      isError = isError,
      placeholder = { Text(placeholder) },
      trailingIcon = trailingIcon,
      colors = ExposedDropdownMenuDefaults.textFieldColors()
    )

    DateInputStyle.Outlined -> OutlinedTextField(
      readOnly = true,
      value = TextFieldValue(selectedMonthText),
      onValueChange = {},
      modifier = modifier,
      label = label,
      isError = isError,
      placeholder = { Text(placeholder) },
      trailingIcon = trailingIcon,
      colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
    )
  }
}

@Composable
private fun ExposedDropdownMenuBoxScope.MonthDropdownMenu(
  isMonthsExpanded: MutableState<Boolean>,
  monthDisplayNames: List<AnnotatedString>,
  onMonthChange: (Int) -> Unit
) {
  ExposedDropdownMenu(
    expanded = isMonthsExpanded.value,
    onDismissRequest = {
      isMonthsExpanded.value = false
    }
  ) {
    monthDisplayNames.forEachIndexed { index, month ->
      DropdownMenuItem(
        onClick = {
          isMonthsExpanded.value = false
          onMonthChange(index + 1) // month is 1 based
        }
      ) {
        Text(text = month)
      }
    }
  }
}
