package com.app.spendable.domain.wallet

import com.app.spendable.data.db.Subscription
import com.app.spendable.data.db.Transaction
import com.app.spendable.presentation.wallet.SubscriptionFrequency
import com.app.spendable.presentation.wallet.SubscriptionIcon
import com.app.spendable.presentation.wallet.SubscriptionItemModel
import com.app.spendable.presentation.wallet.TransactionItemModel
import com.app.spendable.presentation.wallet.TransactionType
import com.app.spendable.utils.DateUtils
import com.app.spendable.utils.toEnum
import java.math.BigDecimal
import java.time.LocalDate

fun Transaction.toItemModel() =
    TransactionItemModel(
        id = id,
        type = type.toEnum<TransactionType>() ?: TransactionType.OTHER,
        title = title,
        subtitle = description,
        cost = BigDecimal(cost)
    )

fun Subscription.toItemModel(today: LocalDate) =
    SubscriptionItemModel(
        id = id,
        iconType = iconType.toEnum<SubscriptionIcon>() ?: SubscriptionIcon.OTHER,
        title = title,
        cost = BigDecimal(cost),
        date = DateUtils.Parse.fromDate(date).let { startDate ->
            if (startDate.dayOfMonth < today.dayOfMonth) {
                today.withDayOfMonth(startDate.dayOfMonth).plusMonths(1)
            } else {
                today.withDayOfMonth(startDate.dayOfMonth)
            }
        },
        frequency = frequency.toEnum<SubscriptionFrequency>() ?: SubscriptionFrequency.MONTHLY,
    )