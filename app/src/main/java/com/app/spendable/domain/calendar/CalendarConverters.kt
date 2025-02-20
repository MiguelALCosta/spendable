package com.app.spendable.domain.calendar

import com.app.spendable.domain.Month
import com.app.spendable.domain.settings.AppCurrency
import com.app.spendable.presentation.calendar.CalendarAdapterModel
import java.math.BigDecimal

fun Month.toAdapterModel(currency: AppCurrency) =
    CalendarAdapterModel.MonthCard(
        month = date,
        budget = totalBudget,
        spent = totalSpent ?: BigDecimal("0"),
        currency = currency
    )