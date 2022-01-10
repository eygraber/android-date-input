package com.eygraber.date_input.xml

import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputLayout
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale
import java.util.concurrent.CopyOnWriteArrayList

internal class DateInputMonthView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = R.attr.textInputExposedDropdownMenuStyle
) : TextInputLayout(
  ContextThemeWrapper(
    context,
    context.obtainStyledAttributes(intArrayOf(R.attr.dateInputViewStyle)).use {
      if(it.getResourceId(0, 0) == 0) {
        val tv = TypedValue()
        if(context.theme.resolveAttribute(R.attr.textInputExposedDropdownMenuStyle, tv, true)) {
          tv.resourceId
        }
        else {
          it.getResourceId(0, R.style.Widget_DateInputView)
        }
      }
      else {
        it.getResourceId(0, R.style.Widget_DateInputView)
      }
    }
  ),
  attrs,
  defStyleAttr
)

internal class DateInputTextLayoutView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = R.attr.textInputStyle
) : TextInputLayout(
  ContextThemeWrapper(
    context,
    context.obtainStyledAttributes(intArrayOf(R.attr.dateInputViewStyle)).use {
      if(it.getResourceId(0, 0) == 0) {
        val tv = TypedValue()
        if(context.theme.resolveAttribute(R.attr.textInputStyle, tv, true)) {
          tv.resourceId
        }
        else {
          it.getResourceId(0, R.style.Widget_DateInputView)
        }
      }
      else {
        it.getResourceId(0, R.style.Widget_DateInputView)
      }
    }
  ),
  attrs,
  defStyleAttr
)

class DateInputView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = R.attr.dateInputViewStyle,
  defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {
  fun interface OnDateChangedListener {
    fun onDateChanged(date: LocalDate?)
  }

  private val monthContainerView: TextInputLayout
  private val monthView: AutoCompleteTextView
  private val dayContainerView: TextInputLayout
  private val dayView: TextView
  private val yearContainerView: TextInputLayout
  private val yearView: TextView
  private val errorView: TextView

  private val dateChangedListeners = CopyOnWriteArrayList<OnDateChangedListener>()

  private var selectedMonth = INVALID_MONTH

  var selectedDate: LocalDate?
    get() = runCatching {
      LocalDate.of(
        yearView.text.toString().toIntOrNull() ?: error("A year needs to be set"),
        selectedMonth,
        dayView.text.toString().toIntOrNull() ?: error("A day needs to be set")
      )
    }.getOrNull()
      ?.takeIf {
        val isEqualToOrGreaterThanMin = minDate == null || it >= minDate
        val isEqualToOrLessThanMax = maxDate == null || it <= maxDate
        isEqualToOrGreaterThanMin && isEqualToOrLessThanMax
      }
    set(value) {
      if(value == null) {
        errorView.text = null
        errorView.isVisible = false
        monthView.text = null
        dayView.text = null
        yearView.text = null
      }
      else {
        monthView.listSelection = value.monthValue
        dayView.text = value.dayOfMonth.toString()
        yearView.text = value.year.toString()
      }
    }

  var error: CharSequence?
    get() = errorView.text.takeIf { it.isNotBlank() }
    set(value) {
      errorView.text = value
      errorView.isVisible = !value.isNullOrBlank()
      monthContainerView.error = " ".takeIf { value != null }
      dayContainerView.error = " ".takeIf { value != null }
      yearContainerView.error = " ".takeIf { value != null }
    }

  var minDate: LocalDate? = null
  var maxDate: LocalDate? = null

  fun addOnDateChangedListener(dateChangedListener: OnDateChangedListener) {
    dateChangedListeners += dateChangedListener
  }

  fun removeOnDateChangedListener(dateChangedListener: OnDateChangedListener) {
    dateChangedListeners -= dateChangedListener
  }

  private fun notifyDateChangedListeners() {
    val date = selectedDate
    for(listener in dateChangedListeners) {
      listener.onDateChanged(date)
    }
  }

  init {
    LayoutInflater
      .from(context)
      .inflate(R.layout.date_input_view, this, true)

    monthContainerView = findViewById(R.id.monthContainer)
    dayContainerView = findViewById(R.id.dayContainer)
    yearContainerView = findViewById(R.id.yearContainer)

    monthView = findViewById<AutoCompleteTextView>(R.id.month).apply {
      onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
        selectedMonth = position + 1
        notifyDateChangedListeners()
      }
    }

    dayView = findViewById<TextView>(R.id.day).apply {
      filters += OnlyDigitsFilter

      doAfterTextChanged {
        notifyDateChangedListeners()
      }
    }

    yearView = findViewById<TextView>(R.id.year).apply {
      filters += OnlyDigitsFilter

      doAfterTextChanged {
        notifyDateChangedListeners()
      }
    }

    errorView = findViewById(R.id.error)

    styledAttr(attrs, R.styleable.DateInputView, defStyleAttr, defStyleRes) {
      monthContainerView.hint = getString(R.styleable.DateInputView_date_input_view_month_hint)
        ?: context.getString(R.string.date_input_view_month_hint)

      monthView.hint = getString(R.styleable.DateInputView_date_input_view_month_placeholder)
        ?: context.getString(R.string.date_input_view_month_placeholder)

      dayContainerView.hint = getString(R.styleable.DateInputView_date_input_view_day_hint)
        ?: context.getString(R.string.date_input_view_day_hint)

      yearContainerView.hint = getString(R.styleable.DateInputView_date_input_view_year_hint)
        ?: context.getString(R.string.date_input_view_year_hint)

      error = getString(R.styleable.DateInputView_date_input_view_error_text)
      getDimensionPixelSize(R.styleable.DateInputView_date_input_view_error_margin_start, -1).let { margin ->
        if(margin >= 0) {
          errorView.updateLayoutParams<LayoutParams> {
            marginStart = margin
          }
        }
      }

      monthView.apply {
        val monthNames =
          when(val resId = getResourceId(R.styleable.DateInputView_date_input_view_month_names, -1)) {
            -1 -> generateLocalizedMonthNames()
            else -> resources.getStringArray(resId).toList()
          }

        setAdapter(
          ArrayAdapter(context, android.R.layout.simple_list_item_1, monthNames)
        )
      }
    }
  }

  companion object {
    private fun generateLocalizedMonthNames() =
      (1..12).map { month ->
        Month
          .of(month)
          .getDisplayName(
            TextStyle.FULL,
            Locale.getDefault()
          )
      }

    private const val INVALID_MONTH = -1

    private val OnlyDigitsFilter = InputFilter { source, start, end, _, _, _ ->
      with(StringBuilder()) {
        for(i in start until end) {
          if(source[i].isDigit()) {
            append(source[i])
          }
        }

        when(length) {
          end - start -> null
          else -> toString()
        }
      }
    }
  }
}
