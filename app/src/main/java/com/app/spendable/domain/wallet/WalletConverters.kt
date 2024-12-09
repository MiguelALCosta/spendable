package com.app.spendable.domain.wallet

import com.app.spendable.R
import com.app.spendable.domain.Subscription
import com.app.spendable.domain.Transaction
import com.app.spendable.domain.settings.AppCurrency
import com.app.spendable.presentation.components.SubscriptionItemComponent
import com.app.spendable.presentation.components.TransactionItemComponent
import com.app.spendable.presentation.toIcon
import com.app.spendable.presentation.toIconResource
import com.app.spendable.presentation.wallet.SubscriptionListItemModel
import com.app.spendable.presentation.wallet.WalletAdapterModel
import com.app.spendable.utils.DateUtils
import com.app.spendable.utils.IStringsManager
import com.app.spendable.utils.PriceUtils
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun Transaction.toWalletAdapterModel(currency: AppCurrency) =
    WalletAdapterModel.Transaction(id, toItemConfig(currency))

fun Transaction.toItemConfig(currency: AppCurrency) =
    TransactionItemComponent.SetupConfig(
        icon = type.toIcon(),
        title = title,
        subtitle = description,
        cost = PriceUtils.Format.toPrice(cost, currency),
    )

fun Subscription.toWalletItemModel(
    stringsManager: IStringsManager,
    today: LocalDate,
    currency: AppCurrency
): SubscriptionListItemModel {
    val payDate = DateUtils.Provide.inCurrentMonth(date)
    val isPaid = today >= payDate
    val daysLeft = ChronoUnit.DAYS.between(today, payDate).toInt()

    val footer = when {
        isPaid -> stringsManager.getString(R.string.paid)
        daysLeft == 1 -> stringsManager.getString(R.string.tomorrow)
        else -> stringsManager.getString(R.string.in_x_days).format(daysLeft)
    }

    return SubscriptionListItemModel(
        id = id,
        order = if (isPaid) payDate.dayOfMonth + 31 else daysLeft,
        config = toItemConfig(currency, footer)
    )
}

fun Subscription.toItemConfig(currency: AppCurrency, footer: String) =
    SubscriptionItemComponent.SetupConfig(
        icon = iconType.toIconResource(),
        title = title,
        cost = PriceUtils.Format.toPrice(cost, currency),
        footer = footer,
    )
