package com.app.spendable.domain.monthDetail

import com.app.spendable.R
import com.app.spendable.data.IMonthsRepository
import com.app.spendable.data.ISubscriptionsRepository
import com.app.spendable.data.ITransactionsRepository
import com.app.spendable.data.preferences.IAppPreferences
import com.app.spendable.domain.BaseInteractor
import com.app.spendable.presentation.components.MonthBalanceComponent
import com.app.spendable.presentation.monthDetail.MonthDetailAdapterModel
import com.app.spendable.utils.DateUtils
import com.app.spendable.utils.IStringsManager
import java.math.BigDecimal
import java.time.YearMonth

interface IMonthDetailInteractor {
    fun getModels(yearMonth: YearMonth, completion: (List<MonthDetailAdapterModel>) -> Unit)
}

class MonthDetailInteractor(
    private val stringsManager: IStringsManager,
    private val appPreferences: IAppPreferences,
    private val transactionsRepository: ITransactionsRepository,
    private val subscriptionsRepository: ISubscriptionsRepository,
    private val monthsRepository: IMonthsRepository
) : BaseInteractor(), IMonthDetailInteractor {

    override fun getModels(
        yearMonth: YearMonth,
        completion: (List<MonthDetailAdapterModel>) -> Unit
    ) {
        makeRequest(request = {
            getMonthBalance(yearMonth)
                .plus(getSubscriptions(yearMonth))
                .plus(getTransactions(yearMonth))
        }, completion)
    }

    private suspend fun getMonthBalance(yearMonth: YearMonth): List<MonthDetailAdapterModel> {
        val month = monthsRepository.getByDate(yearMonth)
        val appCurrency = appPreferences.getAppCurrency()
        val totalBudget = month?.totalBudget ?: BigDecimal("0.00")
        val availableBudget = totalBudget - (month?.totalSpent ?: BigDecimal("0.00"))
        val config = MonthBalanceComponent.SetupConfig(
            totalBudget = totalBudget,
            availableBudget = availableBudget,
            currency = appCurrency
        )

        return listOf(MonthDetailAdapterModel.Balance(config))
    }

    private suspend fun getSubscriptions(yearMonth: YearMonth): List<MonthDetailAdapterModel> {
        val currency = appPreferences.getAppCurrency()
        val subscriptions = subscriptionsRepository.getAllActiveInMonth(yearMonth)
            .map { it.toMonthDetailItemModel(yearMonth, currency) }
            .sortedBy { it.order }

        val header = if (subscriptions.isEmpty()) {
            emptyList()
        } else {
            listOf(MonthDetailAdapterModel.Header(stringsManager.getString(R.string.subscriptions)))
        }
        return header.plus(MonthDetailAdapterModel.SubscriptionsList(subscriptions))
    }

    private suspend fun getTransactions(yearMonth: YearMonth): List<MonthDetailAdapterModel> {
        val currency = appPreferences.getAppCurrency()
        return transactionsRepository.getByMonth(yearMonth)
            .groupBy { it.date.toLocalDate() }
            .toList()
            .sortedByDescending { it.first }
            .flatMap { (date, transactions) ->
                listOf(MonthDetailAdapterModel.Header(DateUtils.Format.toWeekdayDayMonth(date)))
                    .plus(transactions.map { it.toMonthDetailAdapterModel(currency) })
            }
    }

}