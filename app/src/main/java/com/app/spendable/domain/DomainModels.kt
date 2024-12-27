package com.app.spendable.domain

import com.app.spendable.domain.subscriptionDetail.SubscriptionCategory
import com.app.spendable.presentation.wallet.SubscriptionFrequency
import com.app.spendable.presentation.wallet.SubscriptionIcon
import com.app.spendable.presentation.wallet.TransactionType
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

data class Transaction(
    val id: Int,
    val type: TransactionType,
    val title: String,
    val description: String?,
    val cost: BigDecimal,
    val date: LocalDateTime
)

data class Subscription(
    val id: Int,
    val category: SubscriptionCategory,
    val iconType: SubscriptionIcon,
    val title: String,
    val cost: BigDecimal,
    val date: LocalDate,
    val frequency: SubscriptionFrequency,
    val cancellationDate: LocalDate?,
    val finalPaymentDate: LocalDate?
)

data class Month(
    val id: Int,
    val date: YearMonth,
    val totalBudget: BigDecimal,
    val totalSpent: BigDecimal?
)

data class TransactionCreationRequest(
    val type: TransactionType,
    val title: String,
    val description: String?,
    val cost: BigDecimal,
    val date: LocalDateTime
)

data class SubscriptionCreationRequest(
    val category: SubscriptionCategory,
    val iconType: SubscriptionIcon,
    val title: String,
    val cost: BigDecimal,
    val date: LocalDate,
    val frequency: SubscriptionFrequency,
)

data class MonthCreationRequest(
    val date: YearMonth,
    val totalBudget: BigDecimal,
    val totalSpent: BigDecimal? = null
)

data class DailyReward(
    val initialPoints: Int,
    val gainedPoints: Int
)