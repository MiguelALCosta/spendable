package com.app.spendable.data

import com.app.spendable.data.db.IAppDatabase
import com.app.spendable.data.db.Transaction

interface ITransactionsRepository {
    suspend fun insert(transaction: Transaction)
}

class TransactionsRepository(private val database: IAppDatabase) : ITransactionsRepository {
    override suspend fun insert(transaction: Transaction) {
        database.transactionDao().insert(transaction)
    }
}