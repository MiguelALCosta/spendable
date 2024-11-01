package com.app.spendable.data

import com.app.spendable.data.db.IAppDatabase
import com.app.spendable.data.db.Subscription

interface ISubscriptionsRepository {
    suspend fun insert(subscription: Subscription)
}

class SubscriptionsRepository(private val database: IAppDatabase) : ISubscriptionsRepository {
    override suspend fun insert(subscription: Subscription) {
        database.subscriptionDao().insert(subscription)
    }
}