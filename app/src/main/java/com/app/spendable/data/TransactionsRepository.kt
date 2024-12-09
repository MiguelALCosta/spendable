package com.app.spendable.data

import com.app.spendable.data.db.IAppDatabase
import com.app.spendable.domain.Transaction
import com.app.spendable.domain.TransactionCreationRequest
import com.app.spendable.utils.DateUtils
import java.time.YearMonth

interface ITransactionsRepository {
    suspend fun getAll(): List<Transaction>
    suspend fun getByMonth(yearMonth: YearMonth): List<Transaction>
    suspend fun getById(id: Int): Transaction
    suspend fun create(request: TransactionCreationRequest)
    suspend fun update(updatedTransaction: Transaction)
    suspend fun delete(id: Int)
    suspend fun deleteAll()
}

class TransactionsRepository(private val database: IAppDatabase) : ITransactionsRepository {

    override suspend fun getAll(): List<Transaction> {
        return database.transactionDao().getAll()
            .map { it.toDomainModel() }
    }

    override suspend fun getByMonth(yearMonth: YearMonth): List<Transaction> {
        val start = DateUtils.Format.toMillis(yearMonth)
        val endExclusive = DateUtils.Format.toMillis(yearMonth.plusMonths(1))
        return database.transactionDao().getByDateRange(start, endExclusive)
            .map { it.toDomainModel() }
    }

    override suspend fun getById(id: Int): Transaction {
        return database.transactionDao().getById(id).toDomainModel()
    }

    override suspend fun create(request: TransactionCreationRequest) {
        database.transactionDao().insert(request.toDBModel())
    }

    override suspend fun update(updatedTransaction: Transaction) {
        database.transactionDao().update(updatedTransaction.toDBModel())
    }

    override suspend fun delete(id: Int) {
        database.transactionDao().delete(id)
    }

    override suspend fun deleteAll() {
        database.transactionDao().deleteAll()
    }
}