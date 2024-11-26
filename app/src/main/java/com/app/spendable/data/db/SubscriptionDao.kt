package com.app.spendable.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SubscriptionDao {
    @Query("SELECT * FROM `subscription`")
    suspend fun getAll(): List<Subscription>

    @Query("SELECT * FROM `subscription` WHERE id=:id")
    suspend fun getById(id: Int): Subscription

    @Insert
    suspend fun insert(subscription: Subscription)

    @Update
    suspend fun update(subscription: Subscription)

    @Query("DELETE FROM `subscription` WHERE id=:id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM `subscription`")
    suspend fun deleteAll()
}