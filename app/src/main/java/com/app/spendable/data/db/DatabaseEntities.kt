package com.app.spendable.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String? = null,
    @ColumnInfo(name = "cost") val cost: String,
    @ColumnInfo(name = "date") val date: String
)

@Entity
data class Subscription(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "iconType") val iconType: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "cost") val cost: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "frequency") val frequency: String,
    @ColumnInfo(name = "endDate") val endDate: String? = null,
)