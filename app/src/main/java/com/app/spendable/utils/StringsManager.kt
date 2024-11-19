package com.app.spendable.utils

import android.content.Context
import androidx.annotation.StringRes

interface IStringsManager {
    fun getString(@StringRes res: Int): String
    fun updateContext(context: Context)
}

class StringsManager(private var context: Context) : IStringsManager {
    override fun getString(res: Int): String {
        return context.getString(res)
    }

    override fun updateContext(context: Context) {
        this.context = context
    }
}