package com.eygraber.date_input.xml

import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.annotation.StyleableRes
import androidx.core.content.res.use

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
