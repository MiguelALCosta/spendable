package com.app.spendable.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TransactionDao {
    @Query("SELECT * FROM `transactions`")
    suspend fun getAll(): List<TransactionDBModel>

    @Query("SELECT * FROM `transactions` WHERE id=:id")
    suspend fun getById(id: Int): TransactionDBModel

    @Insert
    suspend fun insert(transaction: TransactionDBModel)

    @Update
    suspend fun update(transaction: TransactionDBModel)

    @Query("DELETE FROM `transactions` WHERE id=:id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM `transactions`")
    suspend fun deleteAll()
}