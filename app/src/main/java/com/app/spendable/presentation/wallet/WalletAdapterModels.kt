package com.app.spendable.presentation.wallet

import com.app.spendable.domain.settings.AppCurrency
import com.app.spendable.presentation.components.SubscriptionItemComponent
import com.app.spendable.presentation.components.TransactionItemComponent
import com.app.spendable.presentation.components.WalletCardComponent
import java.math.BigDecimal

sealed interface WalletAdapterModel {

    data class WalletCard(
        val totalBudget: BigDecimal,
        val currency: AppCurrency,
        val config: WalletCardComponent.SetupConfig
    ) : WalletAdapterModel

    data class Header(val text: String) : WalletAdapterModel

    data class Transaction(
        val id: Int,
        val config: TransactionItemComponent.SetupConfig
    ) : WalletAdapterModel

    data class Subscription(
        val id: Int,
        val order: Int,
        val config: SubscriptionItemComponent.SetupConfig
    ) : WalletAdapterModel

    data class SubscriptionsList(val items: List<Subscription>) : WalletAdapterModel

}

enum class TransactionType {
    EAT_OUT, MARKET, SHOPPING, TRANSPORTS, ENTERTAINMENT, BILLS, HOLIDAYS, HEALTH, EDUCATION, PET,
    KIDS, OTHER
}

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
