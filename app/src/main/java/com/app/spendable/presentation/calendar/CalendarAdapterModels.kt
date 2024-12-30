package com.app.spendable.presentation.calendar

import com.app.spendable.domain.settings.AppCurrency
import java.math.BigDecimal
import java.time.YearMonth

sealed interface CalendarAdapterModel {
    data class MonthCard(
        val month: YearMonth,
        val budget: BigDecimal,
        val spent: BigDecimal,
        val currency: AppCurrency
    ) : CalendarAdapterModel

    data class Header(val text: String) : CalendarAdapterModel

    data class Message(val text: String) : CalendarAdapterModel
}