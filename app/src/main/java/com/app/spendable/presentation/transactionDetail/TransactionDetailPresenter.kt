package com.app.spendable.presentation.transactionDetail

import com.app.spendable.domain.transactionDetail.ITransactionDetailInteractor
import com.app.spendable.domain.transactionDetail.TransactionForm

interface IAddTransactionPresenter {
    fun bind(view: AddTransactionView)
    fun unbind()
    fun loadView(id: Int?)
    fun saveTransaction(id: Int?, form: TransactionForm?)
    fun deleteTransaction(id: Int?)
}

class AddTransactionPresenter(
    private val interactor: ITransactionDetailInteractor
) : IAddTransactionPresenter {

    private var view: AddTransactionView? = null

    override fun bind(view: AddTransactionView) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun loadView(id: Int?) {
        interactor.getTransactionForm(id) { form ->
            view?.setupView(form)
        }
    }

    override fun saveTransaction(id: Int?, form: TransactionForm?) {
        if (form == null) {
            view?.showGenericError()
        } else {
            interactor.saveTransactionForm(id, form) { success ->
                if (success) {
                    view?.close()
                } else {
                    view?.setAmountErrorState(form.amount == null)
                    view?.setTitleErrorState(form.title == null)
                    view?.setCategoryErrorState(form.selectedCategory == null)
                }
            }
        }
    }

    override fun deleteTransaction(id: Int?) {
        if (id == null) {
            view?.showGenericError()
        } else {
            interactor.deleteTransaction(id) {
                view?.close()
            }
        }
    }
}