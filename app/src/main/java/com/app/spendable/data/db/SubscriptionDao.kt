package com.app.spendable.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SubscriptionDao {
    @Query("SELECT * FROM `subscription`")
    suspend fun getAll(): List<Subscription>

    @Insert
    suspend fun insert(subscription: Subscription)

    @Delete
    suspend fun delete(subscription: Subscription)
}