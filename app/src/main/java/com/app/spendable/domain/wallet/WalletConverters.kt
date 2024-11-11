package com.app.spendable.domain.wallet

import com.app.spendable.data.db.Subscription
import com.app.spendable.data.db.Transaction
import com.app.spendable.presentation.components.TransactionDetailModel
import com.app.spendable.presentation.wallet.SubscriptionFrequency
import com.app.spendable.presentation.wallet.SubscriptionIcon
import com.app.spendable.presentation.wallet.SubscriptionItemModel
import com.app.spendable.presentation.wallet.TransactionItemModel
import com.app.spendable.presentation.wallet.TransactionType
import com.app.spendable.utils.DateUtils
import java.math.BigDecimal

fun Transaction.toItemModel() =
    TransactionItemModel(
        id = id,
        type = TransactionType.entries.firstOrNull { it.name == type } ?: TransactionType.OTHER,
        title = title,
        subtitle = description,
        cost = BigDecimal(cost)
    )

fun Subscription.toItemModel() =
    SubscriptionItemModel(
        id = id,
        iconType = SubscriptionIcon.entries.firstOrNull { it.name == iconType }
            ?: SubscriptionIcon.OTHER,
        title = title,
        cost = BigDecimal(cost),
        frequency = SubscriptionFrequency.entries.firstOrNull { it.name == frequency }
            ?: SubscriptionFrequency.MONTHLY,
    )

fun Transaction.toDetailModel() =
    TransactionDetailModel(
        id = id,
        type = TransactionType.entries.firstOrNull { it.name == type } ?: TransactionType.OTHER,
        title = title,
        description = description,
        cost = BigDecimal(cost),
        date = DateUtils.Parse.fromDateTime(date)
    )