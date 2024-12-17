package com.app.spendable.utils

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes

fun Context.getColorFromAttr(@AttrRes resId: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(resId, typedValue, true)
    return typedValue.data
}