package com.eygraber.date_input.compose

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp

@Composable
internal fun DateInputText(
  value: Int?,
  onValueChange: (Int?) -> Unit,
  style: DateInputStyle,
  label: @Composable () -> Unit,
  maxLength: Int,
  minWidth: Dp,
  isError: Boolean
) {
  val text = value?.toString() ?: ""
  val modifier = Modifier.width(minWidth)

  val onTextChange = { newText: String ->
    onValueChange(newText.filter { it.isDigit() }.take(maxLength).toIntOrNull())
  }

  when(style) {
    DateInputStyle.Filled -> TextField(
      value = text,
      onValueChange = onTextChange,
      modifier = modifier,
      label = label,
      isError = isError,
      keyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Number
      )
    )

    DateInputStyle.Outlined -> OutlinedTextField(
      value = text,
      onValueChange = onTextChange,
      modifier = modifier,
      label = label,
      isError = isError,
      keyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Number
      )
    )
  }
}
