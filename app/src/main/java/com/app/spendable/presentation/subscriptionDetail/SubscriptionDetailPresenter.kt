package com.app.spendable.presentation.subscriptionDetail

import com.app.spendable.domain.subscriptionDetail.ISubscriptionDetailInteractor
import com.app.spendable.domain.subscriptionDetail.SubscriptionForm

interface ISubscriptionDetailPresenter {
    fun bind(view: ISubscriptionDetailView)
    fun unbind()
    fun loadView(id: Int?)
    fun saveSubscription(id: Int?, form: SubscriptionForm?)
    fun cancelSubscription(id: Int?)
}

class SubscriptionDetailPresenter(
    private val interactor: ISubscriptionDetailInteractor
) : ISubscriptionDetailPresenter {

    private var view: ISubscriptionDetailView? = null

    override fun bind(view: ISubscriptionDetailView) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun loadView(id: Int?) {
        interactor.getSubscriptionForm(id) { form ->
            view?.setupView(form)
            if (id != null) {
                view?.showCancelButton()
            }
        }
    }

    override fun saveSubscription(id: Int?, form: SubscriptionForm?) {
        if (form == null) {
            view?.showGenericError()
        } else {
            interactor.saveSubscriptionForm(id, form) { success ->
                if (success) {
                    view?.close()
                } else {
                    view?.setAmountErrorState(form.amount == null)
                    view?.setTitleErrorState(form.title == null)
                    view?.setCategoryErrorState(form.selectedCategory == null)
                    view?.setSubCategoryErrorState(form.selectedSubcategory == null)
                }
            }
        }
    }

    override fun cancelSubscription(id: Int?) {
        if (id == null) {
            view?.showGenericError()
        } else {
            interactor.cancelSubscription(id) {
                view?.close()
            }
        }
    }
}