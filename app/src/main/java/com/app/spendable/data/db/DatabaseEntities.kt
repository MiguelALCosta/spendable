package com.app.spendable.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionDBModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String? = null,
    @ColumnInfo(name = "cost") val cost: String,
    @ColumnInfo(name = "date") val date: Long
)

@Entity(tableName = "subscriptions")
data class SubscriptionDBModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "iconType") val iconType: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "cost") val cost: String,
    @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "frequency") val frequency: String,
    @ColumnInfo(name = "cancellationDate") val cancellationDate: Long? = null,
    @ColumnInfo(name = "finalPaymentDate") val finalPaymentDate: Long? = null
)

@Entity(tableName = "months")
data class MonthDBModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "totalBudget") val totalBudget: String,
    @ColumnInfo(name = "totalSpent") val totalSpent: String? = null
)