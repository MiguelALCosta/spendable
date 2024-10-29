package com.app.spendable.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.app.spendable.R
import com.app.spendable.presentation.wallet.SubscriptionIcon
import com.app.spendable.presentation.wallet.SubscriptionRecurrency
import com.app.spendable.presentation.wallet.TransactionType

@DrawableRes
fun TransactionType.toIcon() = when (this) {
    TransactionType.EAT_OUT -> R.drawable.ic_eat_out
    TransactionType.MARKET -> R.drawable.ic_market
    TransactionType.SHOPPING -> R.drawable.ic_shopping
    TransactionType.TRANSPORTS -> R.drawable.ic_transports
    TransactionType.ENTERTAINMENT -> R.drawable.ic_entertainment
    TransactionType.BILLS -> R.drawable.ic_bills
    TransactionType.HOLIDAYS -> R.drawable.ic_holidays
    TransactionType.HEALTH -> R.drawable.ic_health
    TransactionType.EDUCATION -> R.drawable.ic_education
    TransactionType.PET -> R.drawable.ic_pet
    TransactionType.KIDS -> R.drawable.ic_kids
    TransactionType.OTHERS -> R.drawable.ic_others
}

@StringRes
fun TransactionType.toTitleRes() = when (this) {
    TransactionType.EAT_OUT -> R.string.eat_out_label
    TransactionType.MARKET -> R.string.market_label
    TransactionType.SHOPPING -> R.string.shopping_label
    TransactionType.TRANSPORTS -> R.string.transports_label
    TransactionType.ENTERTAINMENT -> R.string.entertainment_label
    TransactionType.BILLS -> R.string.bills_label
    TransactionType.HOLIDAYS -> R.string.holidays_label
    TransactionType.HEALTH -> R.string.health_label
    TransactionType.EDUCATION -> R.string.education_label
    TransactionType.PET -> R.string.pet_label
    TransactionType.KIDS -> R.string.kids_label
    TransactionType.OTHERS -> R.string.others_label
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