package com.app.spendable.presentation.wallet

import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

sealed interface WalletAdapterModel

data class WalletCardModel(
    val month: YearMonth,
    val budget: BigDecimal,
    val spent: BigDecimal
) : WalletAdapterModel

data class HeaderModel(val text: String) : WalletAdapterModel

enum class TransactionType {
    EAT_OUT, MARKET, SHOPPING, TRANSPORTS, ENTERTAINMENT, BILLS, HOLIDAYS, HEALTH, EDUCATION, PET,
    KIDS, OTHER
}

data class TransactionItemModel(
    val id: Int,
    val type: TransactionType,
    val title: String,
    val subtitle: String? = null,
    val cost: BigDecimal
) : WalletAdapterModel

enum class SubscriptionIcon {
    NETFLIX, HBO_MAX, DISNEY_PLUS, APPLE_TV, CRUNCHYROLL, DAZN, OTHER_STREAMING,
    APPLE_MUSIC, SPOTIFY, AUDIBLE, OTHER_MUSIC,
    AMAZON_PRIME, YOUTUBE_PREMIUM, OTHER_MULTI_SERVICE,
    DISCORD_NITRO, OTHER_GAMING,
    SPORTS,
    RENT, PHONE, CABLE, OTHER_UTILITIES,
    OTHER
}

enum class SubscriptionFrequency {
    Yearly, MONTHLY, BIWEEKLY, WEEKLY, DAILY
}

data class SubscriptionItemModel(
    val id: Int,
    val iconType: SubscriptionIcon,
    val title: String,
    val cost: BigDecimal,
    val date: LocalDate,
    val frequency: SubscriptionFrequency
) : WalletAdapterModel

data class SubscriptionsListModel(val items: List<SubscriptionItemModel>) : WalletAdapterModel