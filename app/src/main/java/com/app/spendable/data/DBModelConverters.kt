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
        date = DateUtils.Parse.fromDateTime(date)
    )

fun TransactionCreationRequest.toDBModel() =
    TransactionDBModel(
        type = type.name,
        title = title,
        description = description,
        cost = cost.toString(),
        date = DateUtils.Format.toDateTime(date)
    )

fun Transaction.toDBModel() =
    TransactionDBModel(
        id = id,
        type = type.name,
        title = title,
        description = description,
        cost = cost.toString(),
        date = DateUtils.Format.toDateTime(date)
    )

fun SubscriptionDBModel.toDomainModel() =
    Subscription(
        id = id,
        category = category.toEnum<SubscriptionCategory>() ?: SubscriptionCategory.OTHER,
        iconType = iconType.toEnum<SubscriptionIcon>() ?: SubscriptionIcon.OTHER,
        title = title,
        cost = BigDecimal(cost),
        date = DateUtils.Parse.fromDate(date),
        frequency = frequency.toEnum<SubscriptionFrequency>() ?: SubscriptionFrequency.MONTHLY,
        endDate = endDate?.let { DateUtils.Parse.fromDate(it) },
    )

fun Subscription.toDBModel() =
    SubscriptionDBModel(
        id = id,
        category = category.name,
        iconType = iconType.name,
        title = title,
        cost = cost.toString(),
        date = DateUtils.Format.toDate(date),
        frequency = frequency.name,
        endDate = endDate?.let { DateUtils.Format.toDate(it) },
    )

fun SubscriptionCreationRequest.toDBModel() =
    SubscriptionDBModel(
        category = category.name,
        iconType = iconType.name,
        title = title,
        cost = cost.toString(),
        date = DateUtils.Format.toDate(date),
        frequency = frequency.name,
        endDate = null,
    )

fun MonthDBModel.toDomainModel() =
    Month(
        id = id,
        date = DateUtils.Parse.fromYearMonth(date),
        totalBudget = BigDecimal(totalBudget),
        totalSpent = totalSpent?.let { BigDecimal(it) }
    )

fun Month.toDBModel() =
    MonthDBModel(
        id = id,
        date = DateUtils.Format.toYearMonth(date),
        totalBudget = totalBudget.toString(),
        totalSpent = totalSpent?.toString()
    )

fun MonthCreationRequest.toDBModel() =
    MonthDBModel(
        date = DateUtils.Format.toYearMonth(date),
        totalBudget = totalBudget.toString(),
        totalSpent = totalSpent?.toString()
    )