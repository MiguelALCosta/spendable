package com.app.spendable.domain.subscriptionDetail

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.app.spendable.R
import com.app.spendable.domain.Subscription
import com.app.spendable.domain.SubscriptionCreationRequest
import com.app.spendable.domain.settings.AppCurrency
import com.app.spendable.domain.transactionDetail.ExpenseDetailMode
import com.app.spendable.presentation.components.SelectableChoiceComponent
import com.app.spendable.presentation.toIconResource
import com.app.spendable.presentation.toStringResource
import com.app.spendable.presentation.wallet.SubscriptionFrequency
import com.app.spendable.presentation.wallet.SubscriptionIcon
import com.app.spendable.utils.DateUtils
import com.app.spendable.utils.IStringsManager
import com.app.spendable.utils.PriceUtils
import com.app.spendable.utils.toEnum
import java.math.BigDecimal
import java.time.YearMonth
import java.time.ZoneOffset

@StringRes
fun SubscriptionCategory.toStringResource() =
    when (this) {
        SubscriptionCategory.STREAMING -> R.string.streaming_label
        SubscriptionCategory.MUSIC -> R.string.music_label
        SubscriptionCategory.MULTI_SERVICES -> R.string.multi_services_label
        SubscriptionCategory.GAMING -> R.string.gaming_label
        SubscriptionCategory.SPORTS -> R.string.sports_label
        SubscriptionCategory.UTILITIES -> R.string.utilities_label
        SubscriptionCategory.OTHER -> R.string.other_label
    }

@DrawableRes
fun SubscriptionCategory.toIcon() =
    when (this) {
        SubscriptionCategory.STREAMING -> R.drawable.ic_streaming
        SubscriptionCategory.MUSIC -> R.drawable.ic_music
        SubscriptionCategory.MULTI_SERVICES -> R.drawable.ic_multi_services
        SubscriptionCategory.GAMING -> R.drawable.ic_gaming
        SubscriptionCategory.SPORTS -> R.drawable.ic_sports
        SubscriptionCategory.UTILITIES -> R.drawable.ic_utilities
        SubscriptionCategory.OTHER -> R.drawable.ic_other_subscription
    }

@StringRes
fun SubscriptionIcon.toStringResource() =
    when (this) {
        SubscriptionIcon.NETFLIX -> R.string.netflix_label
        SubscriptionIcon.HBO_MAX -> R.string.hbo_max_label
        SubscriptionIcon.DISNEY_PLUS -> R.string.disney_plus_label
        SubscriptionIcon.APPLE_TV -> R.string.apple_tv_label
        SubscriptionIcon.CRUNCHYROLL -> R.string.crunchyroll_label
        SubscriptionIcon.DAZN -> R.string.dazn_label
        SubscriptionIcon.OTHER_STREAMING -> R.string.other_label
        SubscriptionIcon.APPLE_MUSIC -> R.string.apple_music_label
        SubscriptionIcon.SPOTIFY -> R.string.spotify_label
        SubscriptionIcon.AUDIBLE -> R.string.audible_label
        SubscriptionIcon.OTHER_MUSIC -> R.string.other_label
        SubscriptionIcon.AMAZON_PRIME -> R.string.amazon_prime_label
        SubscriptionIcon.YOUTUBE_PREMIUM -> R.string.youtube_premium_label
        SubscriptionIcon.OTHER_MULTI_SERVICE -> R.string.other_label
        SubscriptionIcon.DISCORD_NITRO -> R.string.discord_nitro_label
        SubscriptionIcon.OTHER_GAMING -> R.string.other_label
        SubscriptionIcon.SPORTS -> R.string.sports_label
        SubscriptionIcon.RENT -> R.string.rent_label
        SubscriptionIcon.PHONE -> R.string.phone_label
        SubscriptionIcon.CABLE -> R.string.cable_label
        SubscriptionIcon.OTHER_UTILITIES -> R.string.other_label
        SubscriptionIcon.OTHER -> R.string.other_label
    }


fun SubscriptionCategory.getSubCategoryChoices(stringsManager: IStringsManager) =
    when (this) {
        SubscriptionCategory.STREAMING -> listOf(
            SubscriptionIcon.NETFLIX,
            SubscriptionIcon.HBO_MAX,
            SubscriptionIcon.DISNEY_PLUS,
            SubscriptionIcon.APPLE_TV,
            SubscriptionIcon.CRUNCHYROLL,
            SubscriptionIcon.DAZN,
            SubscriptionIcon.OTHER_STREAMING
        )

        SubscriptionCategory.MUSIC -> listOf(
            SubscriptionIcon.APPLE_MUSIC,
            SubscriptionIcon.SPOTIFY,
            SubscriptionIcon.AUDIBLE,
            SubscriptionIcon.OTHER_MUSIC
        )

        SubscriptionCategory.MULTI_SERVICES -> listOf(
            SubscriptionIcon.AMAZON_PRIME,
            SubscriptionIcon.YOUTUBE_PREMIUM,
            SubscriptionIcon.OTHER_MULTI_SERVICE
        )

        SubscriptionCategory.GAMING -> listOf(
            SubscriptionIcon.DISCORD_NITRO,
            SubscriptionIcon.OTHER_GAMING
        )

        SubscriptionCategory.SPORTS -> null
        SubscriptionCategory.UTILITIES -> listOf(
            SubscriptionIcon.RENT,
            SubscriptionIcon.PHONE,
            SubscriptionIcon.CABLE,
            SubscriptionIcon.OTHER_UTILITIES
        )

        SubscriptionCategory.OTHER -> null
    }?.map {
        SelectableChoiceComponent.Choice(
            it.name,
            stringsManager.getString(it.toStringResource()),
            it.toIconResource()
        )
    }

fun Subscription?.toForm(
    stringsManager: IStringsManager,
    currency: AppCurrency
): SubscriptionForm {
    val now = DateUtils.Provide.nowDevice().toLocalDate()
    val selectedDate = this?.date ?: now

    return SubscriptionForm(
        amount = this?.cost?.let { PriceUtils.Format.toAmount(it) },
        categories = SubscriptionCategory.entries.map {
            SelectableChoiceComponent.Choice(
                it.name,
                stringsManager.getString(it.toStringResource()),
                it.toIcon()
            )
        },
        subcategories = SubscriptionCategory.entries.associate {
            it.name to it.getSubCategoryChoices(stringsManager)
        },
        selectedCategory = this?.category?.name,
        selectedSubcategory = this?.iconType?.name,
        title = this?.title,
        date = selectedDate,
        frequencies = SubscriptionFrequency.entries.map {
            SelectableChoiceComponent.Choice(
                it.name,
                stringsManager.getString(it.toStringResource()),
                null
            )
        },
        selectedFrequency = this?.frequency?.name ?: SubscriptionFrequency.MONTHLY.name,
        currency = currency,
        minDatePickerMillis = YearMonth.from(selectedDate).atDay(1).atStartOfDay()
            .toInstant(ZoneOffset.UTC).toEpochMilli(),
        maxDatePickerMillis = YearMonth.from(selectedDate).atEndOfMonth().atStartOfDay()
            .toInstant(ZoneOffset.UTC).toEpochMilli(),
        cancelState = SubscriptionCancelState(
            visible = this != null,
            enabled = this?.cancellationDate == null,
            text = this?.cancellationDate?.let {
                stringsManager.getString(R.string.cancelled_at).format(it)
            } ?: stringsManager.getString(R.string.cancel_subscription)
        ),
        mode = when {
            this == null -> ExpenseDetailMode.CREATE
            YearMonth.from(selectedDate) == YearMonth.from(now) && cancellationDate == null -> ExpenseDetailMode.EDITABLE
            else -> ExpenseDetailMode.READ_ONLY
        }
    )
}

private fun SubscriptionForm.getIconType() =
    when (selectedCategory?.toEnum<SubscriptionCategory>()) {
        SubscriptionCategory.OTHER -> SubscriptionIcon.OTHER
        SubscriptionCategory.SPORTS -> SubscriptionIcon.SPORTS
        else -> selectedSubcategory?.toEnum<SubscriptionIcon>()
    }

private fun SubscriptionForm.getFinalTitle(): String? {
    return if (needsTitle()) {
        title
    } else {
        val choices = selectedCategory?.let { subcategories.getOrDefault(it, null) }
        selectedSubcategory?.let { sub -> choices?.firstOrNull { it.id == sub } }
            ?.label
    }
}

fun SubscriptionForm.toSubscription(id: Int): Subscription? {
    return Subscription(
        id = id,
        category = selectedCategory?.toEnum<SubscriptionCategory>() ?: return null,
        iconType = getIconType() ?: return null,
        title = getFinalTitle()?.ifBlank { null } ?: return null,
        cost = amount?.ifBlank { null }?.let { BigDecimal(it) } ?: return null,
        date = date,
        frequency = selectedFrequency.toEnum<SubscriptionFrequency>() ?: return null,
        cancellationDate = null,
        finalPaymentDate = null
    )
}

fun SubscriptionForm.toCreationRequest(): SubscriptionCreationRequest? {
    return SubscriptionCreationRequest(
        category = selectedCategory?.toEnum<SubscriptionCategory>() ?: return null,
        iconType = getIconType() ?: return null,
        title = getFinalTitle()?.ifBlank { null } ?: return null,
        cost = amount?.ifBlank { null }?.let { BigDecimal(it) } ?: return null,
        date = date,
        frequency = selectedFrequency.toEnum<SubscriptionFrequency>() ?: return null
    )
}