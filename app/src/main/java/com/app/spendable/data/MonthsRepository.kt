package com.app.spendable.data

import com.app.spendable.data.db.IAppDatabase
import com.app.spendable.data.db.Month
import com.app.spendable.utils.DateUtils
import java.time.YearMonth

interface IMonthsRepository {
    suspend fun getAll(): List<Month>
    suspend fun getByDate(yearMonth: YearMonth): Month?
    suspend fun insert(month: Month)
    suspend fun update(month: Month)
    suspend fun deleteAll()
}

class MonthsRepository(private val database: IAppDatabase) : IMonthsRepository {

    override suspend fun getAll(): List<Month> {
        return database.monthDao().getAll()
    }

    override suspend fun getByDate(yearMonth: YearMonth): Month? {
        return database.monthDao().getByDate(DateUtils.Format.toYearMonth(yearMonth))
    }

    override suspend fun insert(month: Month) {
        database.monthDao().insert(month)
    }

    override suspend fun update(month: Month) {
        database.monthDao().update(month)
    }

    override suspend fun deleteAll() {
        database.monthDao().deleteAll()
    }
}