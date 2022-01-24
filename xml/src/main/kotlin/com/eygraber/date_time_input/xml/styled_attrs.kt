@file:Suppress("NOTHING_TO_INLINE")

package com.eygraber.date_time_input.xml

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.annotation.StyleableRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.res.use

internal inline fun Context.createDateInputWrapper(
  @AttrRes attrToResolve: Int
) = createInputWrapper(
  style = R.attr.dateInputViewStyle,
  default = R.style.Widget_DateTimeInput,
  attrToResolve = attrToResolve
)

internal inline fun Context.createTimeInputWrapper(
  @AttrRes attrToResolve: Int
) = createInputWrapper(
  style = R.attr.timeInputViewStyle,
  default = R.style.Widget_DateTimeInput,
  attrToResolve = attrToResolve
)

private inline fun Context.createInputWrapper(
  @AttrRes style: Int,
  @StyleRes default: Int,
  @AttrRes attrToResolve: Int
) = ContextThemeWrapper(
  this,
  obtainStyledAttributes(intArrayOf(style)).let {
    try {
      if(it.getResourceId(0, 0) == 0) {
        val tv = TypedValue()
        if(theme.resolveAttribute(attrToResolve, tv, true)) {
          tv.resourceId
        }
        else {
          it.getResourceId(0, default)
        }
      }
      else {
        it.getResourceId(0, default)
      }
    }
    finally {
      it.recycle()
    }
  }
)

internal inline fun View.styledAttr(
  attrs: AttributeSet?,
  @StyleableRes styleableRes: IntArray,
  @AttrRes defStyleAttr: Int,
  @StyleRes defStyleRes: Int = 0,
  block: TypedArray.() -> Unit
) {
  context
    .obtainStyledAttributes(attrs, styleableRes, defStyleAttr, defStyleRes)
    .use(block)
}
