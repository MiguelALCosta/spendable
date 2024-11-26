package com.app.spendable.data

import com.app.spendable.data.db.IAppDatabase
import com.app.spendable.data.db.Subscription

interface ISubscriptionsRepository {
    suspend fun getAll(): List<Subscription>
    suspend fun getById(id: Int): Subscription
    suspend fun insert(subscription: Subscription)
    suspend fun update(subscription: Subscription)
    suspend fun delete(id: Int)
    suspend fun deleteAll()
}

class SubscriptionsRepository(private val database: IAppDatabase) : ISubscriptionsRepository {

    override suspend fun getAll(): List<Subscription> {
        return database.subscriptionDao().getAll()
    }

    override suspend fun getById(id: Int): Subscription {
        return database.subscriptionDao().getById(id)
    }

    override suspend fun insert(subscription: Subscription) {
        database.subscriptionDao().insert(subscription)
    }

    override suspend fun update(subscription: Subscription) {
        database.subscriptionDao().update(subscription)
    }

    override suspend fun delete(id: Int) {
        return database.subscriptionDao().delete(id)
    }

    override suspend fun deleteAll() {
        database.subscriptionDao().deleteAll()
    }
}