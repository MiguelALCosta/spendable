package com.app.spendable.presentation.add.subscription

import com.app.spendable.data.ISubscriptionsRepository
import com.app.spendable.presentation.components.SelectableChoiceComponent
import com.app.spendable.presentation.toStringResource
import com.app.spendable.presentation.wallet.SubscriptionFrequency
import com.app.spendable.utils.DateUtils
import com.app.spendable.utils.IStringsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface IAddSubscriptionPresenter {
    fun bind(view: AddSubscriptionView)
    fun unbind()
    fun loadView(id: Int?)
    fun saveSubscription(id: Int?, form: AddSubscriptionForm?)
    fun cancelSubscription(id: Int?)
}

class AddSubscriptionPresenter(
    private val stringsManager: IStringsManager,
    private val subscriptionsRepository: ISubscriptionsRepository
) : IAddSubscriptionPresenter {

    private var view: AddSubscriptionView? = null

    override fun bind(view: AddSubscriptionView) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun loadView(id: Int?) {
        CoroutineScope(Dispatchers.Main).launch {
            val subscription = withContext(Dispatchers.IO) {
                id?.let { subscriptionsRepository.getById(it) }
            }

            val now = DateUtils.Provide.nowDevice()
            val form = AddSubscriptionForm(
                amount = subscription?.cost,
                categories = SubscriptionCategory.entries.map {
                    SelectableChoiceComponent.Choice(
                        it.name,
                        stringsManager.getString(it.toStringResource()),
                        it.toIcon()
                    )
                },
                subcategories = SubscriptionCategory.entries.associate {
                    it.name to it.getSubCategoryChoices(stringsManager)
                },
                selectedCategory = subscription?.category,
                selectedSubcategory = subscription?.iconType,
                title = subscription?.title,
                date = subscription?.date?.let { DateUtils.Parse.fromDate(it) }
                    ?: now.toLocalDate(),
                frequencies = SubscriptionFrequency.entries.map {
                    SelectableChoiceComponent.Choice(
                        it.name,
                        stringsManager.getString(it.toStringResource()),
                        null
                    )
                },
                selectedFrequency = SubscriptionFrequency.entries.firstOrNull { it.name == subscription?.frequency }?.name
                    ?: SubscriptionFrequency.MONTHLY.name
            )
            view?.setupView(form)
            if (id != null) {
                view?.showCancelButton()
            }
        }
    }

    override fun saveSubscription(id: Int?, form: AddSubscriptionForm?) {
        if (form == null) {
            view?.showGenericError()
            return
        }

        val subscription = form.toSubscription()
        if (subscription == null) {
            view?.setAmountErrorState(form.amount == null)
            view?.setTitleErrorState(form.title == null)
            view?.setCategoryErrorState(form.selectedCategory == null)
            view?.setSubCategoryErrorState(form.selectedSubcategory == null)
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                val result = withContext(Dispatchers.IO) {
                    if (id == null) {
                        subscriptionsRepository.insert(subscription)
                    } else {
                        subscriptionsRepository.update(subscription.copy(id = id))
                    }
                    true
                }
                if (result) {
                    view?.close()
                }
            }
        }
    }

    override fun cancelSubscription(id: Int?) {
        CoroutineScope(Dispatchers.Main).launch {
            val result = withContext(Dispatchers.IO) {
                id?.let { subscriptionsRepository.getById(it) }
                    ?.let { subscription ->
                        val now = DateUtils.Provide.nowDevice().toLocalDate()
                        val nowFormatted = DateUtils.Format.toDate(now)
                        subscriptionsRepository.update(subscription.copy(endDate = nowFormatted))
                    }
                true
            }
            if (result) {
                view?.close()
            }
        }
    }
}