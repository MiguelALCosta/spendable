package com.app.spendable.presentation.add.transaction

import com.app.spendable.data.ITransactionsRepository
import com.app.spendable.presentation.components.SelectableChoiceComponent
import com.app.spendable.presentation.toIcon
import com.app.spendable.presentation.toTitleRes
import com.app.spendable.presentation.wallet.TransactionType
import com.app.spendable.utils.DateUtils
import com.app.spendable.utils.IStringsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface IAddTransactionPresenter {
    fun bind(view: AddTransactionView)
    fun unbind()
    fun loadView()
    fun saveTransaction(form: AddTransactionForm?)
}

class AddTransactionPresenter(
    private val stringsManager: IStringsManager,
    private val transactionsRepository: ITransactionsRepository,
) : IAddTransactionPresenter {

    private var view: AddTransactionView? = null

    override fun bind(view: AddTransactionView) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun loadView() {
        val now = DateUtils.Provide.nowDevice()
        val form = AddTransactionForm(
            amount = null,
            title = null,
            categories = TransactionType.entries.map {
                SelectableChoiceComponent.Choice(
                    it.name,
                    stringsManager.getString(it.toTitleRes()),
                    it.toIcon()
                )
            },
            selectedCategory = null,
            date = now.toLocalDate(),
            time = now.toLocalTime(),
            notes = null,
        )
        view?.setupView(form)
    }

    override fun saveTransaction(form: AddTransactionForm?) {
        if (form == null) {
            view?.showGenericError()
            return
        }

        val transaction = form.toTransaction()
        if (transaction == null) {
            view?.setAmountErrorState(form.amount == null)
            view?.setTitleErrorState(form.title == null)
            view?.setCategoryErrorState(form.selectedCategory == null)
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO) {
                    transactionsRepository.insert(transaction)
                }
                view?.closeAdd()
            }
        }
    }
}