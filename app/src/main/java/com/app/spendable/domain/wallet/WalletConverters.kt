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
import com.app.spendable.utils.toEnum
import java.math.BigDecimal

fun Transaction.toItemModel() =
    TransactionItemModel(
        id = id,
        type = type.toEnum<TransactionType>() ?: TransactionType.OTHER,
        title = title,
        subtitle = description,
        cost = BigDecimal(cost)
    )

fun Subscription.toItemModel() =
    SubscriptionItemModel(
        id = id,
        iconType = iconType.toEnum<SubscriptionIcon>() ?: SubscriptionIcon.OTHER,
        title = title,
        cost = BigDecimal(cost),
        frequency = frequency.toEnum<SubscriptionFrequency>() ?: SubscriptionFrequency.MONTHLY,
    )

fun Transaction.toDetailModel() =
    TransactionDetailModel(
        id = id,
        type = type.toEnum<TransactionType>() ?: TransactionType.OTHER,
        title = title,
        description = description,
        cost = BigDecimal(cost),
        date = DateUtils.Parse.fromDateTime(date)
    )