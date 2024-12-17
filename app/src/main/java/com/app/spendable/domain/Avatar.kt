package com.app.spendable.domain

import androidx.annotation.DrawableRes
import com.app.spendable.R

enum class Avatar(val requiredPoints: Int, @DrawableRes val drawableRes: Int) {
    BASE(0, R.drawable.dummy_avatar_1),
    NEXT(10, R.drawable.dummy_avatar_2),
    ANOTHER_ONE(50, R.drawable.dummy_avatar_3);

    companion object {
        val progressList = entries.sortedBy { it.requiredPoints }
    }
}

