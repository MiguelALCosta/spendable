package com.app.spendable.domain.monthDetail

import com.app.spendable.R
import com.app.spendable.data.IMonthsRepository
import com.app.spendable.data.ISubscriptionsRepository
import com.app.spendable.data.ITransactionsRepository
import com.app.spendable.data.preferences.IAppPreferences
import com.app.spendable.domain.BaseInteractor
import com.app.spendable.presentation.monthDetail.MonthDetailAdapterModel
import com.app.spendable.utils.DateUtils
import com.app.spendable.utils.IStringsManager
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
            getSubscriptions(yearMonth)
                .plus(getTransactions(yearMonth))
        }, completion)
    }

    /*private suspend fun getWalletCard(): List<WalletAdapterModel> {
        val today = DateUtils.Provide.nowDevice().toLocalDate()
        val currentMonth = YearMonth.from(today)

        val transactionsSum = transactionsRepository.getAll()
            .filter { YearMonth.from(it.date) == currentMonth }
            .sumOf { it.cost }

        val subscriptionsSum = subscriptionsRepository.getAll()
            .filter {
                val payDate = DateUtils.Provide.inCurrentMonth(it.date)
                payDate <= today && (it.endDate == null || it.endDate >= today)
            }
            .sumOf { it.cost }

        val appCurrency = appPreferences.getAppCurrency()
        val totalBudget = getCurrentMonthModel()?.totalBudget ?: BigDecimal("0.00")
        val availableBudget = totalBudget - transactionsSum - subscriptionsSum
        val config = WalletCardComponent.SetupConfig(
            title = DateUtils.Format.toFullMonthYear(currentMonth),
            totalBudget = PriceUtils.Format.toPrice(totalBudget, appCurrency),
            availableBudget = PriceUtils.Format.toPrice(availableBudget, appCurrency),
            percentage = (availableBudget * BigDecimal("100") / totalBudget).toInt(),
            isEditable = true
        )

        return listOf(WalletAdapterModel.WalletCard(totalBudget, appCurrency, config))
    }*/

    /*private suspend fun getCurrentMonthModel(): Month? {
        val currentMonth = YearMonth.from(DateUtils.Provide.nowDevice())
        return monthsRepository.getByDate(currentMonth) ?: run {
            val lastKnownTotalBudget =
                monthsRepository.getAll().maxByOrNull { it.date }?.totalBudget
            val request = MonthCreationRequest(
                date = currentMonth,
                totalBudget = lastKnownTotalBudget ?: AppConstants.DEFAULT_MONTHLY_BUDGET
            )
            monthsRepository.create(request)
            monthsRepository.getByDate(currentMonth)
        }
    }*/

    private suspend fun getSubscriptions(yearMonth: YearMonth): List<MonthDetailAdapterModel> {
        val currency = appPreferences.getAppCurrency()
        val subscriptions = subscriptionsRepository.getAll()
            .filter {
                val payDate = DateUtils.Provide.inMonth(yearMonth, it.date)
                YearMonth.from(it.date) <= yearMonth
                        && (it.endDate == null || it.endDate >= payDate)
            }
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
        return transactionsRepository.getAll()
            .filter { YearMonth.from(it.date) == yearMonth }
            .groupBy { it.date.toLocalDate() }
            .toList()
            .sortedByDescending { it.first }
            .flatMap { (date, transactions) ->
                listOf(MonthDetailAdapterModel.Header(DateUtils.Format.toWeekdayDayMonth(date)))
                    .plus(transactions.map { it.toMonthDetailAdapterModel(currency) })
            }
    }

}