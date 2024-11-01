package com.app.spendable.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.app.spendable.R
import com.app.spendable.presentation.wallet.SubscriptionFrequency
import com.app.spendable.presentation.wallet.SubscriptionIcon
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
    TransactionType.OTHER -> R.drawable.ic_other_transaction
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
    TransactionType.OTHER -> R.string.other_label
}

@DrawableRes
fun SubscriptionIcon.toIconResource() = when (this) {
    SubscriptionIcon.NETFLIX -> R.drawable.ic_netflix
    SubscriptionIcon.HBO_MAX -> R.drawable.ic_hbo_max
    SubscriptionIcon.DISNEY_PLUS -> R.drawable.ic_disney_plus
    SubscriptionIcon.APPLE_TV -> R.drawable.ic_apple_tv
    SubscriptionIcon.CRUNCHYROLL -> R.drawable.ic_crunchyroll
    SubscriptionIcon.DAZN -> R.drawable.ic_dazn
    SubscriptionIcon.OTHER_STREAMING -> R.drawable.ic_streaming
    SubscriptionIcon.APPLE_MUSIC -> R.drawable.ic_apple_music
    SubscriptionIcon.SPOTIFY -> R.drawable.ic_spotify
    SubscriptionIcon.AUDIBLE -> R.drawable.ic_audible
    SubscriptionIcon.OTHER_MUSIC -> R.drawable.ic_music
    SubscriptionIcon.AMAZON_PRIME -> R.drawable.ic_amazon
    SubscriptionIcon.YOUTUBE_PREMIUM -> R.drawable.ic_youtube
    SubscriptionIcon.OTHER_MULTI_SERVICE -> R.drawable.ic_multi_services
    SubscriptionIcon.DISCORD_NITRO -> R.drawable.ic_discord
    SubscriptionIcon.OTHER_GAMING -> R.drawable.ic_gaming
    SubscriptionIcon.SPORTS -> R.drawable.ic_sports
    SubscriptionIcon.RENT -> R.drawable.ic_other_subscription
    SubscriptionIcon.PHONE -> R.drawable.ic_other_subscription
    SubscriptionIcon.CABLE -> R.drawable.ic_other_subscription
    SubscriptionIcon.OTHER_UTILITIES -> R.drawable.ic_utilities
    SubscriptionIcon.OTHER -> R.drawable.ic_other_subscription
}

@StringRes
fun SubscriptionFrequency.toStringResource() = when (this) {
    SubscriptionFrequency.Yearly -> R.string.yearly_label
    SubscriptionFrequency.MONTHLY -> R.string.monthly_label
    SubscriptionFrequency.BIWEEKLY -> R.string.biweekly_stats
    SubscriptionFrequency.WEEKLY -> R.string.weekly_stats
    SubscriptionFrequency.DAILY -> R.string.daily_stats
}