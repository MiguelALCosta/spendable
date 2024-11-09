package com.app.spendable.data

import com.app.spendable.data.db.IAppDatabase
import com.app.spendable.data.db.Subscription

interface ISubscriptionsRepository {
    suspend fun getAll(): List<Subscription>
    suspend fun insert(subscription: Subscription)
}

class SubscriptionsRepository(private val database: IAppDatabase) : ISubscriptionsRepository {

    override suspend fun getAll(): List<Subscription> {
        return database.subscriptionDao().getAll()
    }

    override suspend fun insert(subscription: Subscription) {
        database.subscriptionDao().insert(subscription)
    }
}