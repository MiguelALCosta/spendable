package com.app.spendable.data

import com.app.spendable.data.db.IAppDatabase
import com.app.spendable.data.db.Transaction

interface ITransactionsRepository {
    suspend fun getAll(): List<Transaction>
    suspend fun getById(id: Int): Transaction
    suspend fun insert(transaction: Transaction)
    suspend fun delete(id: Int)
}

class TransactionsRepository(private val database: IAppDatabase) : ITransactionsRepository {

    override suspend fun getAll(): List<Transaction> {
        return database.transactionDao().getAll()
    }

    override suspend fun getById(id: Int): Transaction {
        return database.transactionDao().getById(id)
    }

    override suspend fun insert(transaction: Transaction) {
        database.transactionDao().insert(transaction)
    }

    override suspend fun delete(id: Int) {
        database.transactionDao().delete(id)
    }
}