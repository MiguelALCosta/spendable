package com.app.spendable.domain.monthDetail

import com.app.spendable.domain.Subscription
import com.app.spendable.domain.Transaction
import com.app.spendable.domain.settings.AppCurrency
import com.app.spendable.domain.wallet.toItemConfig
import com.app.spendable.presentation.monthDetail.MonthDetailAdapterModel
import com.app.spendable.presentation.wallet.SubscriptionListItemModel
import com.app.spendable.utils.DateUtils
import java.time.YearMonth

fun Transaction.toMonthDetailAdapterModel(currency: AppCurrency) =
    MonthDetailAdapterModel.Transaction(id, toItemConfig(currency))

fun Subscription.toMonthDetailItemModel(
    yearMonth: YearMonth,
    currency: AppCurrency
): SubscriptionListItemModel {
    val payDate = DateUtils.Provide.inMonth(yearMonth, date)
    val footer = DateUtils.Format.toDate(payDate)
    return SubscriptionListItemModel(
        id = id,
        order = payDate.dayOfMonth,
        config = toItemConfig(currency, footer)
    )
}