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
    fun loadView()
    fun saveSubscription(form: AddSubscriptionForm?)
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

    override fun loadView() {
        val now = DateUtils.Provide.nowDevice()
        val form = AddSubscriptionForm(
            amount = null,
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
            selectedCategory = null,
            selectedSubcategory = null,
            title = null,
            date = now.toLocalDate(),
            frequencies = SubscriptionFrequency.entries.map {
                SelectableChoiceComponent.Choice(
                    it.name,
                    stringsManager.getString(it.toStringResource()),
                    null
                )
            },
            selectedFrequency = SubscriptionFrequency.MONTHLY.name
        )
        view?.setupView(form)
    }

    override fun saveSubscription(form: AddSubscriptionForm?) {
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
                withContext(Dispatchers.IO) {
                    subscriptionsRepository.insert(subscription)
                }
                view?.closeAdd()
            }
        }
    }
}