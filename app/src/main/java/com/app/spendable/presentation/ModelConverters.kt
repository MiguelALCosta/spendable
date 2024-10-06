package com.app.spendable.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.app.spendable.R
import com.app.spendable.presentation.wallet.SubscriptionIcon
import com.app.spendable.presentation.wallet.SubscriptionRecurrency
import com.app.spendable.presentation.wallet.TransactionType

@DrawableRes
fun TransactionType.toIcon() = when (this) {
    TransactionType.DUMMY -> R.drawable.ic_wallet
}

@DrawableRes
fun SubscriptionIcon.toIconResource() = when (this) {
    SubscriptionIcon.DUMMY -> R.drawable.ic_wallet
}

@StringRes
fun SubscriptionRecurrency.toStringResource() = when (this) {
    SubscriptionRecurrency.MONTHLY -> R.string.monthly_label
    SubscriptionRecurrency.BIWEEKLY -> R.string.biweekly_stats
    SubscriptionRecurrency.WEEKLY -> R.string.weekly_stats
    SubscriptionRecurrency.DAILY -> R.string.daily_stats
}