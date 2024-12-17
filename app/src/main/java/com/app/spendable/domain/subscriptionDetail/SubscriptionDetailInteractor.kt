package com.app.spendable.domain.subscriptionDetail

import com.app.spendable.data.ISubscriptionsRepository
import com.app.spendable.data.preferences.IAppPreferences
import com.app.spendable.domain.BaseInteractor
import com.app.spendable.utils.DateUtils
import com.app.spendable.utils.IStringsManager
import java.time.YearMonth

interface ISubscriptionDetailInteractor {
    fun getSubscriptionForm(id: Int?, completion: (SubscriptionForm) -> Unit)
    fun saveSubscriptionForm(id: Int?, form: SubscriptionForm, completion: (Boolean) -> Unit)
    fun cancelSubscription(id: Int, completion: (Unit) -> Unit)
    fun deleteSubscription(id: Int, completion: (Unit) -> Unit)
}

class SubscriptionDetailInteractor(
    private val stringsManager: IStringsManager,
    private val appPreferences: IAppPreferences,
    private val subscriptionsRepository: ISubscriptionsRepository
) : BaseInteractor(), ISubscriptionDetailInteractor {

    override fun getSubscriptionForm(id: Int?, completion: (SubscriptionForm) -> Unit) {
        makeRequest(request = {
            val subscription = id?.let { subscriptionsRepository.getById(it) }
            subscription.toForm(stringsManager, appPreferences.getAppCurrency())
        }, completion)
    }

    override fun saveSubscriptionForm(
        id: Int?,
        form: SubscriptionForm,
        completion: (Boolean) -> Unit
    ) {
        makeRequest(request = {
            if (id == null) {
                form.toCreationRequest()?.let { request ->
                    subscriptionsRepository.create(request)
                    true
                } ?: false
            } else {
                form.toSubscription(id)?.let { updatedSubscription ->
                    subscriptionsRepository.update(updatedSubscription)
                    true
                } ?: false
            }
        }, completion)
    }

    override fun cancelSubscription(id: Int, completion: (Unit) -> Unit) {
        makeRequest(request = {
            val subscription = subscriptionsRepository.getById(id)
            val now = DateUtils.Provide.nowDevice().toLocalDate()
            val payDate = DateUtils.Provide.inCurrentMonth(subscription.date)
            val finalPaymentDate = when {
                payDate <= now -> payDate // already paid this month
                YearMonth.from(subscription.date) == YearMonth.from(now) -> null // never paid
                else -> payDate.minusMonths(1) // paid last month
            }
            val cancelledSubscription = subscription.copy(
                cancellationDate = now,
                finalPaymentDate = finalPaymentDate
            )
            subscriptionsRepository.update(cancelledSubscription)
        }, completion)
    }

    override fun deleteSubscription(id: Int, completion: (Unit) -> Unit) {
        makeRequest(request = {
            subscriptionsRepository.delete(id)
        }, completion)
    }

}