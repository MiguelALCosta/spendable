package com.app.spendable.data

import com.app.spendable.data.db.IAppDatabase
import com.app.spendable.domain.Subscription
import com.app.spendable.domain.SubscriptionCreationRequest
import com.app.spendable.utils.DateUtils
import java.time.YearMonth

interface ISubscriptionsRepository {
    suspend fun getAll(): List<Subscription>
    suspend fun getAllActiveInMonth(yearMonth: YearMonth): List<Subscription>
    suspend fun getById(id: Int): Subscription
    suspend fun create(request: SubscriptionCreationRequest)
    suspend fun update(updatedSubscription: Subscription)
    suspend fun delete(id: Int)
    suspend fun deleteAll()
}

class SubscriptionsRepository(private val database: IAppDatabase) : ISubscriptionsRepository {

    override suspend fun getAll(): List<Subscription> {
        return database.subscriptionDao().getAll()
            .map { it.toDomainModel() }
    }

    override suspend fun getAllActiveInMonth(yearMonth: YearMonth): List<Subscription> {
        val startedBeforeDate = DateUtils.Format.toMillis(yearMonth.plusMonths(1))
        val finalPaymentFromDate = DateUtils.Format.toMillis(yearMonth)
        return database.subscriptionDao()
            .getAllActiveInPeriod(startedBeforeDate, finalPaymentFromDate)
            .map { it.toDomainModel() }
    }

    override suspend fun getById(id: Int): Subscription {
        return database.subscriptionDao().getById(id).toDomainModel()
    }

    override suspend fun create(request: SubscriptionCreationRequest) {
        database.subscriptionDao().insert(request.toDBModel())
    }

    override suspend fun update(updatedSubscription: Subscription) {
        database.subscriptionDao().update(updatedSubscription.toDBModel())
    }

    override suspend fun delete(id: Int) {
        return database.subscriptionDao().delete(id)
    }

    override suspend fun deleteAll() {
        database.subscriptionDao().deleteAll()
    }
}