package com.app.spendable.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TransactionDao {
    @Query("SELECT * FROM `transaction`")
    suspend fun getAll(): List<Transaction>

    @Query("SELECT * FROM `transaction` WHERE id=:id")
    suspend fun getById(id: Int): Transaction

    @Insert
    suspend fun insert(transaction: Transaction)

    @Update
    suspend fun update(transaction: Transaction)

    @Query("DELETE FROM `transaction` WHERE id=:id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM `transaction`")
    suspend fun deleteAll()
}