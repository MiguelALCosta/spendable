package com.app.spendable.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SubscriptionDao {
    @Query("SELECT * FROM `subscriptions`")
    suspend fun getAll(): List<SubscriptionDBModel>

    @Query(
        "SELECT * FROM `subscriptions` WHERE date<:startedBeforeDate AND " +
                "(cancellationDate IS NULL OR (finalPaymentDate IS NOT NULL AND finalPaymentDate>=:finalPaymentFromDate))"
    )
    suspend fun getAllActiveInPeriod(
        startedBeforeDate: Long,
        finalPaymentFromDate: Long
    ): List<SubscriptionDBModel>

    @Query("SELECT * FROM `subscriptions` WHERE id=:id")
    suspend fun getById(id: Int): SubscriptionDBModel

    @Insert
    suspend fun insert(subscription: SubscriptionDBModel)

    @Update
    suspend fun update(subscription: SubscriptionDBModel)

    @Query("DELETE FROM `subscriptions` WHERE id=:id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM `subscriptions`")
    suspend fun deleteAll()
}