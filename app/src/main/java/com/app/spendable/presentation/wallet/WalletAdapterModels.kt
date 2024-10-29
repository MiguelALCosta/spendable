package com.app.spendable.presentation.wallet

import java.math.BigDecimal

sealed interface WalletAdapterModel

data class WalletCardModel(val budget: BigDecimal, val spent: BigDecimal) : WalletAdapterModel

data class HeaderModel(val text: String) : WalletAdapterModel

enum class TransactionType {
    EAT_OUT, MARKET, SHOPPING, TRANSPORTS, ENTERTAINMENT, BILLS, HOLIDAYS, HEALTH, EDUCATION, PET,
    KIDS, OTHERS
}

data class TransactionItemModel(
    val type: TransactionType,
    val title: String,
    val subtitle: String? = null,
    val cost: BigDecimal
) : WalletAdapterModel

enum class SubscriptionIcon {
    DUMMY
}

enum class SubscriptionRecurrency {
    MONTHLY, BIWEEKLY, WEEKLY, DAILY
}

data class SubscriptionItemModel(
    val iconType: SubscriptionIcon,
    val title: String,
    val cost: BigDecimal,
    val recurrency: SubscriptionRecurrency
)

data class SubscriptionsListModel(val items: List<SubscriptionItemModel>) : WalletAdapterModel