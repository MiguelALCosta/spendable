package com.app.spendable.utils

import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import com.app.spendable.domain.settings.AppCurrency
import com.app.spendable.presentation.toSymbol
import java.math.BigDecimal
import java.util.Locale

object PriceUtils {

    object Format {

        private fun getFormatter(): DecimalFormat {
            val symbols = DecimalFormatSymbols(Locale.getDefault())
            symbols.decimalSeparator = '.'
            return DecimalFormat("0.00", symbols)
        }

        fun toAmount(bigDecimal: BigDecimal): String {
            return try {
                getFormatter().format(bigDecimal)
            } catch (_: Throwable) {
                "0.00"
            }
        }

        fun toCorrectedAmount(str: String): String {
            return try {
                getFormatter().format(BigDecimal(str))
            } catch (_: Throwable) {
                "0.00"
            }
        }

        fun toCorrectedPartialAmount(str: String): String {
            val segments = str.split(".")
            val left = segments.getOrNull(0) ?: ""
            val right = segments.getOrNull(1)?.take(2) ?: ""
            val separator = if (segments.count() > 1) "." else ""
            return left + separator + right
        }

        fun toPrice(bigDecimal: BigDecimal, currency: AppCurrency) =
            toAmount(bigDecimal) + currency.toSymbol()

    }

    object Parse {

        fun fromAmount(str: String) =
            BigDecimal(Format.toCorrectedAmount(str))

    }

}