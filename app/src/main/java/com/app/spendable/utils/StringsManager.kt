package com.app.spendable.utils

import android.content.Context
import androidx.annotation.StringRes

interface IStringsManager {
    fun getString(@StringRes res: Int): String
}

class StringsManager(private val context: Context) : IStringsManager {
    override fun getString(res: Int): String {
        return context.getString(res)
    }
}