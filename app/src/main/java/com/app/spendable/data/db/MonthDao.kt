package com.app.spendable.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MonthDao {
    @Query("SELECT * FROM `month`")
    suspend fun getAll(): List<Month>

    @Query("SELECT * FROM `month` WHERE date=:date")
    suspend fun getByDate(date: String): Month?

    @Insert
    suspend fun insert(month: Month)

    @Update
    suspend fun update(month: Month)

    @Query("DELETE FROM `month`")
    suspend fun deleteAll()
}