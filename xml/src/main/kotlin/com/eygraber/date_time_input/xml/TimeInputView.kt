@file:SuppressLint("SetTextI18n")

package com.eygraber.date_time_input.xml

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.format.DateFormat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doAfterTextChanged
import com.eygraber.date_time_input.common.TimeResult
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputLayout
import java.time.LocalTime
import java.util.concurrent.CopyOnWriteArrayList

internal class TimeInputTextLayoutView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : TextInputLayout(
  context.createTimeInputWrapper(R.attr.textInputStyle),
  attrs
)

internal class TimeDropdownMenuView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : TextInputLayout(
  context.createTimeInputWrapper(R.attr.textInputExposedDropdownMenuStyle),
  attrs
)

class TimeInputView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
  fun interface OnTimeChangedListener {
    fun onTimeChanged(timeChangeResult: TimeResult)
  }

  private val flowView: Flow
  private val hourContainerView: TextInputLayout
  private val hourView: TextView
  private val hourLabelView: TextView
  private val minuteContainerView: TextInputLayout
  private val minuteView: TextView
  private val minuteLabelView: TextView
  private val secondContainerView: TextInputLayout
  private val secondView: TextView
  private val secondLabelView: TextView
  private val secondGroupView: Group
  private val amPmContainerView: MaterialButtonToggleGroup
  private val amView: MaterialButton
  private val pmView: MaterialButton
  private val errorView: TextView

  private val timeChangedListeners = CopyOnWriteArrayList<OnTimeChangedListener>()

  private val is24HourTime = DateFormat.is24HourFormat(context)

  private val hourFilter = MaxInputFilter(if(is24HourTime) 23 else 12)
  private val sixtyFilter = MaxInputFilter(59)

  private var padLeadingZero = true

  var hourLabel: CharSequence
    get() = hourLabelView.text
    set(value) {
      hourLabelView.text = value
    }

  var minuteLabel: CharSequence
    get() = minuteLabelView.text
    set(value) {
      minuteLabelView.text = value
    }

  var secondLabel: CharSequence
    get() = secondLabelView.text
    set(value) {
      secondLabelView.text = value
    }

  var amLabel: CharSequence
    get() = amView.text
    set(value) {
      amView.text = value
    }

  var pmLabel: CharSequence
    get() = pmView.text
    set(value) {
      pmView.text = value
    }

  override fun setEnabled(enabled: Boolean) {
    super.setEnabled(enabled)
    hourContainerView.isEnabled = enabled
    minuteContainerView.isEnabled = enabled
    secondContainerView.isEnabled = enabled
    amPmContainerView.isEnabled = enabled
    amPmContainerView.children.forEach { it.isEnabled = enabled }
  }

  val selectedTimeResult: TimeResult
    get() = TimeResult.calculateResult(
      minTime = minTime,
      maxTime = maxTime,
      hour = hourView.text.toString().toIntOrNull()?.let { hour ->
        when {
          is24HourTime -> hour
          else -> {
            val isPm = amPmContainerView.checkedButtonId == R.id.pmView
            hour % 12 + if(isPm) 12 else 0
          }
        }
      },
      minute = minuteView.text.toString().toIntOrNull(),
      second = if(secondContainerView.isVisible) secondView.text.toString().toIntOrNull() else 0
    )

  var selectedTime: LocalTime?
    get() = selectedTimeResult.getOrNull()
    set(value) {
      try {
        suppressChangeNotifications = true

        if(value == null) {
          errorView.text = null
          errorView.isVisible = false
          hourView.text = null
          minuteView.text = null
          secondView.text = null
        }
        else {
          amPmContainerView.check(if(value.hour >= 12) R.id.pmView else R.id.amView)
          val hourDisplay = when {
            is24HourTime -> value.hour
            value.hour % 12 == 0 -> 12
            value.hour >= 12 -> value.hour - 12
            else -> value.hour
          }

          hourView.text = when {
            padLeadingZero -> "%02d".format(hourDisplay)
            else -> hourDisplay.toString()
          }
          minuteView.text = when {
            padLeadingZero -> "%02d".format(value.minute)
            else -> value.minute.toString()
          }
          secondView.text = when {
            padLeadingZero -> "%02d".format(value.second)
            else -> value.second.toString()
          }
        }
      }
      finally {
        suppressChangeNotifications = false
      }
    }

  var error: CharSequence?
    get() = errorView.text.takeIf { it.isNotBlank() }
    set(value) {
      errorView.text = value
      errorView.isVisible = !value.isNullOrBlank()
      hourContainerView.error = " ".takeIf { value != null }
      minuteContainerView.error = " ".takeIf { value != null }
      secondContainerView.error = " ".takeIf { value != null }
    }

  var minTime: LocalTime? = null
  var maxTime: LocalTime? = null

  fun addOnTimeChangedListener(timeChangedListener: OnTimeChangedListener) {
    timeChangedListeners += timeChangedListener
  }

  fun removeOnTimeChangedListener(timeChangedListener: OnTimeChangedListener) {
    timeChangedListeners -= timeChangedListener
  }

  private var suppressChangeNotifications = false
  private fun notifyTimeChangedListeners() {
    if(suppressChangeNotifications) return

    val time = selectedTimeResult
    for(listener in timeChangedListeners) {
      listener.onTimeChanged(time)
    }
  }

  private val afterTextChangedListener = { _: Editable? ->
    notifyTimeChangedListeners()
  }

  private fun EditText.padOnFocusLost() {
    setOnFocusChangeListener { _, hasFocus ->
      if(!hasFocus) {
        if(text.length == 1) {
          text.toString().toIntOrNull()?.let { digit ->
            setText("0$digit")
            setSelection(2)
          }
        }
      }
    }
  }

  init {
    LayoutInflater
      .from(context)
      .inflate(R.layout.time_input_view, this, true)

    flowView = findViewById(R.id.time_flow)
    hourContainerView = findViewById(R.id.hourContainer)
    minuteContainerView = findViewById(R.id.minuteContainer)
    secondContainerView = findViewById(R.id.secondContainer)

    hourView = hourContainerView.findViewById<EditText>(R.id.hour).apply {
      filters += hourFilter

      doAfterTextChanged(afterTextChangedListener)

      if(padLeadingZero) padOnFocusLost()
    }

    hourLabelView = hourContainerView.findViewById(R.id.hourLabel)

    minuteView = minuteContainerView.findViewById<EditText>(R.id.minute).apply {
      filters += sixtyFilter

      doAfterTextChanged(afterTextChangedListener)

      if(padLeadingZero) padOnFocusLost()
    }

    minuteLabelView = minuteContainerView.findViewById(R.id.minuteLabel)

    secondView = secondContainerView.findViewById<EditText>(R.id.second).apply {
      filters += sixtyFilter

      doAfterTextChanged(afterTextChangedListener)

      if(padLeadingZero) padOnFocusLost()
    }

    secondLabelView = secondContainerView.findViewById(R.id.secondLabel)

    secondGroupView = findViewById(R.id.secondGroup)

    amPmContainerView = findViewById<MaterialButtonToggleGroup>(R.id.amPmContainer).apply {
      isVisible = !is24HourTime
    }

    amView = amPmContainerView.findViewById(R.id.amView)
    pmView = amPmContainerView.findViewById(R.id.pmView)

    errorView = findViewById(R.id.error)

    styledAttr(attrs, R.styleable.TimeInputView, R.attr.timeInputViewStyle) {
      val enabled = getBoolean(R.styleable.DateInputView_android_enabled, true)
      isEnabled = enabled

      val horizontalGap = getDimensionPixelSize(
        R.styleable.TimeInputView_timeInputView_horizontalGap,
        -1
      )
      if(horizontalGap > -1) {
        flowView.setHorizontalGap(horizontalGap)
      }

      val verticalGap = getDimensionPixelSize(
        R.styleable.TimeInputView_timeInputView_verticalGap,
        -1
      )
      if(verticalGap > -1) {
        flowView.setVerticalGap(verticalGap)
      }

      flowView.setHorizontalBias(
        getFloat(R.styleable.TimeInputView_timeInputView_horizontalBias, 0F)
      )

      flowView.setVerticalBias(
        getFloat(R.styleable.TimeInputView_timeInputView_verticalBias, 0F)
      )

      flowView.setHorizontalAlign(
        getInt(R.styleable.TimeInputView_timeInputView_flowAlign, Flow.HORIZONTAL_ALIGN_START)
      )

      val secondVisibility = when(
        getInt(R.styleable.TimeInputView_timeInputView_secondVisibility, View.VISIBLE)
      ) {
        0 -> View.VISIBLE
        1 -> View.INVISIBLE
        else -> View.GONE
      }

      secondGroupView.visibility = secondVisibility

      hourLabelView.text = getString(R.styleable.TimeInputView_timeInputView_hourLabel)
        ?: context.getString(R.string.time_input_view_hour_label)

      minuteLabelView.text = getString(R.styleable.TimeInputView_timeInputView_minuteLabel)
        ?: context.getString(R.string.time_input_view_minute_label)

      secondLabelView.text = getString(R.styleable.TimeInputView_timeInputView_secondLabel)
        ?: context.getString(R.string.time_input_view_second_label)

      amView.text = getString(R.styleable.TimeInputView_timeInputView_amLabel)
        ?: context.getString(R.string.time_input_view_am)

      pmView.text = getString(R.styleable.TimeInputView_timeInputView_pmLabel)
        ?: context.getString(R.string.time_input_view_pm)

      getDimensionPixelSize(
        R.styleable.TimeInputView_timeInputView_errorMarginStart, -1
      ).let { margin ->
        if(margin >= 0) {
          errorView.updateLayoutParams<LayoutParams> {
            marginStart = margin
          }
        }
      }

      padLeadingZero = getBoolean(R.styleable.TimeInputView_timeInputView_padLeadingZero, true)
    }
  }

  companion object {
    private class MaxInputFilter(private val max: Int) : InputFilter {
      override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
      ) = runCatching {
        val new = StringBuilder(dest).apply {
          replace(dstart, dend, source.subSequence(start, end).toString())
        }.toString()
        when {
          new.toInt() <= max -> null
          else -> ""
        }
      }.getOrDefault("")
    }
  }
}
