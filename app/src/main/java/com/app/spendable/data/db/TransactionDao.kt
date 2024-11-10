package com.app.spendable.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TransactionDao {
    @Query("SELECT * FROM `transaction`")
    suspend fun getAll(): List<Transaction>

    @Query("SELECT * FROM `transaction` WHERE id=:id")
    suspend fun getById(id: Int): Transaction

    @Insert
    suspend fun insert(transaction: Transaction)

    @Query("DELETE FROM `transaction` WHERE id=:id")
    suspend fun delete(id: Int)
}