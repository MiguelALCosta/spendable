package com.app.spendable.data

import com.app.spendable.data.db.IAppDatabase
import com.app.spendable.domain.Month
import com.app.spendable.domain.MonthCreationRequest
import com.app.spendable.utils.DateUtils
import java.time.YearMonth

interface IMonthsRepository {
    suspend fun getAll(): List<Month>
    suspend fun getByDate(yearMonth: YearMonth): Month?
    suspend fun create(request: MonthCreationRequest)
    suspend fun update(updatedMonth: Month)
    suspend fun deleteAll()
}

class MonthsRepository(private val database: IAppDatabase) : IMonthsRepository {

    override suspend fun getAll(): List<Month> {
        return database.monthDao().getAll()
            .map { it.toDomainModel() }
    }

    override suspend fun getByDate(yearMonth: YearMonth): Month? {
        val formattedYearMonth = DateUtils.Format.toYearMonth(yearMonth)
        return database.monthDao().getByDate(formattedYearMonth)?.toDomainModel()
    }

    override suspend fun create(request: MonthCreationRequest) {
        database.monthDao().insert(request.toDBModel())
    }

    override suspend fun update(updatedMonth: Month) {
        database.monthDao().update(updatedMonth.toDBModel())
    }

    override suspend fun deleteAll() {
        database.monthDao().deleteAll()
    }
}