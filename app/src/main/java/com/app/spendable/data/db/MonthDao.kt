package com.app.spendable.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MonthDao {
    @Query("SELECT * FROM `months`")
    suspend fun getAll(): List<MonthDBModel>

    @Query("SELECT * FROM `months` WHERE date=:date")
    suspend fun getByDate(date: String): MonthDBModel?

    @Insert
    suspend fun insert(month: MonthDBModel)

    @Update
    suspend fun update(month: MonthDBModel)

    @Query("DELETE FROM `months`")
    suspend fun deleteAll()
}