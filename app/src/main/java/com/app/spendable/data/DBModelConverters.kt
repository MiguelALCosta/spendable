package com.app.spendable.data

import com.app.spendable.data.db.MonthDBModel
import com.app.spendable.data.db.SubscriptionDBModel
import com.app.spendable.data.db.TransactionDBModel
import com.app.spendable.domain.Month
import com.app.spendable.domain.MonthCreationRequest
import com.app.spendable.domain.Subscription
import com.app.spendable.domain.SubscriptionCreationRequest
import com.app.spendable.domain.Transaction
import com.app.spendable.domain.TransactionCreationRequest
import com.app.spendable.domain.subscriptionDetail.SubscriptionCategory
import com.app.spendable.presentation.wallet.SubscriptionFrequency
import com.app.spendable.presentation.wallet.SubscriptionIcon
import com.app.spendable.presentation.wallet.TransactionType
import com.app.spendable.utils.DateUtils
import com.app.spendable.utils.toEnum
import java.math.BigDecimal

fun TransactionDBModel.toDomainModel() =
    Transaction(
        id = id,
        type = type.toEnum<TransactionType>() ?: TransactionType.OTHER,
        title = title,
        description = description,
        cost = BigDecimal(cost),
        date = DateUtils.Parse.fromMillisToDateTime(date)
    )

fun TransactionCreationRequest.toDBModel() =
    TransactionDBModel(
        type = type.name,
        title = title,
        description = description,
        cost = cost.toString(),
        date = DateUtils.Format.toMillis(date)
    )

fun Transaction.toDBModel() =
    TransactionDBModel(
        id = id,
        type = type.name,
        title = title,
        description = description,
        cost = cost.toString(),
        date = DateUtils.Format.toMillis(date)
    )

fun SubscriptionDBModel.toDomainModel() =
    Subscription(
        id = id,
        category = category.toEnum<SubscriptionCategory>() ?: SubscriptionCategory.OTHER,
        iconType = iconType.toEnum<SubscriptionIcon>() ?: SubscriptionIcon.OTHER,
        title = title,
        cost = BigDecimal(cost),
        date = DateUtils.Parse.fromMillisToDate(date),
        frequency = frequency.toEnum<SubscriptionFrequency>() ?: SubscriptionFrequency.MONTHLY,
        cancellationDate = cancellationDate?.let { DateUtils.Parse.fromMillisToDate(it) },
        finalPaymentDate = finalPaymentDate?.let { DateUtils.Parse.fromMillisToDate(it) }
    )

fun Subscription.toDBModel() =
    SubscriptionDBModel(
        id = id,
        category = category.name,
        iconType = iconType.name,
        title = title,
        cost = cost.toString(),
        date = DateUtils.Format.toMillis(date),
        frequency = frequency.name,
        cancellationDate = cancellationDate?.let { DateUtils.Format.toMillis(it) },
        finalPaymentDate = finalPaymentDate?.let { DateUtils.Format.toMillis(it) }
    )

fun SubscriptionCreationRequest.toDBModel() =
    SubscriptionDBModel(
        category = category.name,
        iconType = iconType.name,
        title = title,
        cost = cost.toString(),
        date = DateUtils.Format.toMillis(date),
        frequency = frequency.name,
        cancellationDate = null,
        finalPaymentDate = null
    )

fun MonthDBModel.toDomainModel() =
    Month(
        id = id,
        date = DateUtils.Parse.fromMillisToYearMonth(date),
        totalBudget = BigDecimal(totalBudget),
        totalSpent = totalSpent?.let { BigDecimal(it) }
    )

fun Month.toDBModel() =
    MonthDBModel(
        id = id,
        date = DateUtils.Format.toMillis(date),
        totalBudget = totalBudget.toString(),
        totalSpent = totalSpent?.toString()
    )

fun MonthCreationRequest.toDBModel() =
    MonthDBModel(
        date = DateUtils.Format.toMillis(date),
        totalBudget = totalBudget.toString(),
        totalSpent = totalSpent?.toString()
    )