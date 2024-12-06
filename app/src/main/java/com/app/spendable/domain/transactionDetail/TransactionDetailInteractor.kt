package com.app.spendable.domain.transactionDetail

import com.app.spendable.data.ITransactionsRepository
import com.app.spendable.data.preferences.IAppPreferences
import com.app.spendable.domain.BaseInteractor
import com.app.spendable.utils.IStringsManager

interface ITransactionDetailInteractor {
    fun getTransactionForm(id: Int?, completion: (TransactionForm) -> Unit)
    fun saveTransactionForm(id: Int?, form: TransactionForm, completion: (Boolean) -> Unit)
    fun deleteTransaction(id: Int, completion: (Unit) -> Unit)
}

class TransactionDetailInteractor(
    private val stringsManager: IStringsManager,
    private val appPreferences: IAppPreferences,
    private val transactionsRepository: ITransactionsRepository
) : BaseInteractor(), ITransactionDetailInteractor {

    override fun getTransactionForm(id: Int?, completion: (TransactionForm) -> Unit) {
        makeRequest(request = {
            val transaction = id?.let { transactionsRepository.getById(it) }
            transaction.toForm(stringsManager, appPreferences.getAppCurrency())
        }, completion)
    }

    override fun saveTransactionForm(
        id: Int?,
        form: TransactionForm,
        completion: (Boolean) -> Unit
    ) {
        makeRequest(request = {
            if (id == null) {
                form.toCreationRequest()?.let { request ->
                    transactionsRepository.create(request)
                    true
                } ?: false
            } else {
                form.toTransaction(id)?.let { updatedTransaction ->
                    transactionsRepository.update(updatedTransaction)
                    true
                } ?: false
            }
        }, completion)
    }

    override fun deleteTransaction(id: Int, completion: (Unit) -> Unit) {
        makeRequest(request = {
            transactionsRepository.delete(id)
        }, completion)
    }

}