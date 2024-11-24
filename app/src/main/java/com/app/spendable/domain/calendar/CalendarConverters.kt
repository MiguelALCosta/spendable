package com.app.spendable.domain.calendar

import com.app.spendable.data.db.Month
import com.app.spendable.domain.settings.AppCurrency
import com.app.spendable.presentation.calendar.MonthCardModel
import com.app.spendable.utils.DateUtils
import java.math.BigDecimal
import java.time.YearMonth

fun Month.toAdapterModel(currency: AppCurrency) =
    MonthCardModel(
        month = YearMonth.from(DateUtils.Parse.fromYearMonth(date)),
        budget = BigDecimal(totalBudget),
        spent = BigDecimal(totalSpent ?: "0"),
        currency = currency
    )