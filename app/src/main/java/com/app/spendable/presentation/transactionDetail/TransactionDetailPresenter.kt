package com.app.spendable.presentation.transactionDetail

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
    fun loadView(id: Int?)
    fun saveTransaction(id: Int?, form: TransactionForm?)
    fun deleteTransaction(id: Int?)
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

    override fun loadView(id: Int?) {
        CoroutineScope(Dispatchers.Main).launch {
            val transaction = withContext(Dispatchers.IO) {
                id?.let { transactionsRepository.getById(it) }
            }

            val selectedDateTime = transaction?.date?.let {
                DateUtils.Parse.fromDateTime(it)
            } ?: DateUtils.Provide.nowDevice()
            val form = TransactionForm(
                amount = transaction?.cost,
                title = transaction?.title,
                categories = TransactionType.entries.map {
                    SelectableChoiceComponent.Choice(
                        it.name,
                        stringsManager.getString(it.toTitleRes()),
                        it.toIcon()
                    )
                },
                selectedCategory = transaction?.type,
                date = selectedDateTime.toLocalDate(),
                time = selectedDateTime.toLocalTime(),
                notes = transaction?.description,
            )
            view?.setupView(form)

            if (id != null) {
                view?.showDeleteButton()
            }
        }
    }

    override fun saveTransaction(id: Int?, form: TransactionForm?) {
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
                    if (id == null) {
                        transactionsRepository.insert(transaction)
                    } else {
                        transactionsRepository.update(transaction.copy(id = id))
                    }
                }
                view?.close()
            }
        }
    }

    override fun deleteTransaction(id: Int?) {
        if (id == null) {
            view?.showGenericError()
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                val result = withContext(Dispatchers.IO) {
                    transactionsRepository.delete(id)
                    true
                }
                if (result) {
                    view?.close()
                }
            }
        }
    }
}