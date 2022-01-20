package com.eygraber.date_time_input.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize

@Composable
fun DateInput(
  isOutlined: Boolean
) {
  if(isOutlined) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }
    var textfieldSize by remember { mutableStateOf(Size.Zero) }

    MaterialTheme(
      colors = darkColors()
    ) {
      Surface {
        Column {
          OutlinedTextField(
            value = selectedText,
            onValueChange = {
              selectedText = it
            },
            modifier = Modifier
              .fillMaxWidth()
              .onGloballyPositioned { coordinates ->
                textfieldSize = coordinates.size.toSize()
              }
              .clickable {
                expanded = true
              },
            label = {
              Text("Month")
            }
          )

          DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
              .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
          ) {
            arrayOf("January", "February", "March").forEach { month ->
              DropdownMenuItem(
                onClick = { selectedText = month }
              ) {
                Text(month)
              }
            }
          }
        }
      }
    }
  }
}
