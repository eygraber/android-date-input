package com.eygraber.date_input

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eygraber.date_input.xml.DateInputView
import com.eygraber.date_input.xml.DateResult
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class MainActivity : AppCompatActivity() {
  private val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    findViewById<DateInputView>(R.id.date_input_view).apply {
      minDate = LocalDate.of(1950, Month.JANUARY, 1)
      maxDate = LocalDate.of(2050, Month.JANUARY, 1)

      addOnDateChangedListener {
        error = when(it) {
          is DateResult.Success -> null
          is DateResult.Error -> it.error.message

          is DateResult.ViolatedMaxDate ->
            "${it.date.format(formatter)} needs to be less than ${it.maxDate.format(formatter)}"
          is DateResult.ViolatedMinDate -> when {
            it.date.year < 1_000 -> "Date needs to be greater than ${it.minDate.format(formatter)}"
            else -> "${it.date.format(formatter)} needs to be greater than ${it.minDate.format(formatter)}"
          }

          DateResult.RequiresDay -> null
          DateResult.RequiresYear -> null
        }
      }
    }
  }
}
