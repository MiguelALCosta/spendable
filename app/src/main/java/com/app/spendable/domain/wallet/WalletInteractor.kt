package com.app.spendable.domain.wallet

import com.app.spendable.R
import com.app.spendable.data.ISubscriptionsRepository
import com.app.spendable.data.ITransactionsRepository
import com.app.spendable.domain.BaseInteractor
import com.app.spendable.presentation.wallet.HeaderModel
import com.app.spendable.presentation.wallet.SubscriptionsListModel
import com.app.spendable.presentation.wallet.WalletAdapterModel
import com.app.spendable.presentation.wallet.WalletCardModel
import com.app.spendable.utils.DateUtils
import com.app.spendable.utils.IStringsManager
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

interface IWalletInteractor {
    fun getModels(completion: (List<WalletAdapterModel>) -> Unit)
}

class WalletInteractor(
    private val stringsManager: IStringsManager,
    private val transactionsRepository: ITransactionsRepository,
    private val subscriptionsRepository: ISubscriptionsRepository
) : BaseInteractor(), IWalletInteractor {

    override fun getModels(completion: (List<WalletAdapterModel>) -> Unit) {
        makeRequest(request = {
            getWalletCard()
                .plus(getCurrentMonthSubscriptions())
                .plus(getCurrentMonthTransactions())
        }, completion)
    }

    private suspend fun getWalletCard(): List<WalletAdapterModel> {
        return listOf(WalletCardModel(BigDecimal("100.50"), BigDecimal("100.50")))
    }

    private suspend fun getCurrentMonthSubscriptions(): List<WalletAdapterModel> {
        val currentMonth = YearMonth.from(DateUtils.Provide.nowDevice())
        val subscriptions = subscriptionsRepository.getAll()
            .filter {
                YearMonth.from(DateUtils.Parse.fromDate(it.date)) <= currentMonth
                        && (it.endDate == null || YearMonth.from(DateUtils.Parse.fromDate(it.endDate)) >= currentMonth)
            }
            .sortedBy { DateUtils.Parse.fromDate(it.date) }
            .map { it.toItemModel() }
        val header = if (subscriptions.isEmpty()) {
            emptyList()
        } else {
            listOf(HeaderModel(stringsManager.getString(R.string.subscriptions)))
        }
        return header.plus(SubscriptionsListModel(subscriptions))
    }

    private suspend fun getCurrentMonthTransactions(): List<WalletAdapterModel> {
        val currentMonth = YearMonth.from(DateUtils.Provide.nowDevice())
        return transactionsRepository.getAll()
            .map { DateUtils.Parse.fromDateTime(it.date) to it }
            .filter { YearMonth.from(it.first) == currentMonth }
            .groupBy { LocalDate.from(it.first) }
            .toList()
            .sortedByDescending { it.first }
            .flatMap {
                listOf(HeaderModel(DateUtils.Format.toWeekdayDayMonth(it.first)))
                    .plus(it.second.map { it.second.toItemModel() })
            }
    }

}