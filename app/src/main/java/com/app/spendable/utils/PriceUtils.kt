package com.app.spendable.utils

import android.icu.text.DecimalFormat
import com.app.spendable.domain.settings.AppCurrency
import com.app.spendable.presentation.toSymbol
import java.math.BigDecimal

fun BigDecimal.toFormatedPrice(currency: AppCurrency): String {
    val df = DecimalFormat("##.00")
    return df.format(this) + currency.toSymbol()
}