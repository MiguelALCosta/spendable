package com.app.spendable.domain.wallet

import com.app.spendable.R
import com.app.spendable.data.db.Subscription
import com.app.spendable.data.db.Transaction
import com.app.spendable.domain.settings.AppCurrency
import com.app.spendable.presentation.wallet.SubscriptionFrequency
import com.app.spendable.presentation.wallet.SubscriptionIcon
import com.app.spendable.presentation.wallet.SubscriptionItemModel
import com.app.spendable.presentation.wallet.TransactionItemModel
import com.app.spendable.presentation.wallet.TransactionType
import com.app.spendable.utils.DateUtils
import com.app.spendable.utils.IStringsManager
import com.app.spendable.utils.toEnum
import java.math.BigDecimal
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun Transaction.toItemModel(currency: AppCurrency) =
    TransactionItemModel(
        id = id,
        type = type.toEnum<TransactionType>() ?: TransactionType.OTHER,
        title = title,
        subtitle = description,
        cost = BigDecimal(cost),
        currency = currency
    )

fun Subscription.toItemModel(
    stringsManager: IStringsManager,
    today: LocalDate,
    currency: AppCurrency
): SubscriptionItemModel {
    val startDate = DateUtils.Parse.fromDate(date)
    val payDate = DateUtils.Provide.inCurrentMonth(startDate)
    val isPaid = today >= payDate
    val daysLeft = ChronoUnit.DAYS.between(today, payDate).toInt()

    val dateText = when {
        isPaid -> stringsManager.getString(R.string.paid)
        daysLeft == 1 -> stringsManager.getString(R.string.tomorrow)
        else -> stringsManager.getString(R.string.in_x_days).format(daysLeft)
    }

    return SubscriptionItemModel(
        id = id,
        iconType = iconType.toEnum<SubscriptionIcon>() ?: SubscriptionIcon.OTHER,
        title = title,
        cost = BigDecimal(cost),
        dateText = dateText,
        order = if (isPaid) payDate.dayOfMonth + 31 else daysLeft,
        frequency = frequency.toEnum<SubscriptionFrequency>() ?: SubscriptionFrequency.MONTHLY,
        currency = currency
    )
}
