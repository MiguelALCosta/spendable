package com.app.spendable.domain

import androidx.annotation.DrawableRes
import com.app.spendable.R

enum class Avatar(val requiredPoints: Int, @DrawableRes val drawableRes: Int) {
    BASE(0, R.drawable.duck),
    LADY(10, R.drawable.duck_lady),
    RUBBER(300, R.drawable.duck_rubber),
    FRENCH(1000, R.drawable.duck_french);

    companion object {
        val progressList = entries.sortedBy { it.requiredPoints }
    }
}
