package com.app.spendable.domain.wallet

import com.app.spendable.R
import com.app.spendable.data.IMonthsRepository
import com.app.spendable.data.ISubscriptionsRepository
import com.app.spendable.data.ITransactionsRepository
import com.app.spendable.data.db.Month
import com.app.spendable.data.preferences.IAppPreferences
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
    fun deleteTransaction(id: Int, completion: () -> Unit)
    fun updateTotalBudget(newValue: BigDecimal, completion: () -> Unit)
}

class WalletInteractor(
    private val stringsManager: IStringsManager,
    private val appPreferences: IAppPreferences,
    private val transactionsRepository: ITransactionsRepository,
    private val subscriptionsRepository: ISubscriptionsRepository,
    private val monthsRepository: IMonthsRepository
) : BaseInteractor(), IWalletInteractor {

    override fun getModels(completion: (List<WalletAdapterModel>) -> Unit) {
        makeRequest(request = {

            /*val months = monthsRepository.getAll()
            if (months.firstOrNull {
                    it.date == DateUtils.Format.toYearMonth(YearMonth.of(2023, 2))
                } == null) {
                monthsRepository.insert(
                    Month(
                        date = DateUtils.Format.toYearMonth(YearMonth.of(2023, 2)),
                        totalBudget = "1000.00",
                        totalSpent = null
                    )
                )
            }*/

            getWalletCard()
                .plus(getCurrentMonthSubscriptions())
                .plus(getCurrentMonthTransactions())
        }, completion)
    }

    private suspend fun getWalletCard(): List<WalletAdapterModel> {
        val today = DateUtils.Provide.nowDevice().toLocalDate()
        val currentMonth = YearMonth.from(today)

        val transactionsSum = transactionsRepository.getAll()
            .filter { YearMonth.from(DateUtils.Parse.fromDateTime(it.date)) == currentMonth }
            .sumOf { BigDecimal(it.cost) }

        val subscriptionsSum = subscriptionsRepository.getAll()
            .filter {
                val date = DateUtils.Parse.fromDate(it.date)
                val endDate = it.endDate?.let { DateUtils.Parse.fromDate(it) }
                date <= today && (endDate == null || YearMonth.from(endDate) >= currentMonth)
            }
            .sumOf { BigDecimal(it.cost) }

        return listOf(
            WalletCardModel(
                month = currentMonth,
                budget = BigDecimal(getCurrentMonthModel().totalBudget),
                spent = transactionsSum + subscriptionsSum,
                currency = appPreferences.getAppCurrency()
            )
        )
    }

    private suspend fun getCurrentMonthModel(): Month {
        val currentMonth = YearMonth.from(DateUtils.Provide.nowDevice())
        return monthsRepository.getByDate(currentMonth) ?: run {
            val lastKnownTotalBudget = monthsRepository.getAll()
                .sortedByDescending { DateUtils.Parse.fromYearMonth(it.date) }
                .firstOrNull()
                ?.totalBudget
            val newModel = Month(
                date = DateUtils.Format.toYearMonth(currentMonth),
                totalBudget = lastKnownTotalBudget ?: "1000.00"
            )
            monthsRepository.insert(newModel)
            newModel
        }
    }

    private suspend fun getCurrentMonthSubscriptions(): List<WalletAdapterModel> {
        val today = DateUtils.Provide.nowDevice().toLocalDate()
        val currency = appPreferences.getAppCurrency()
        val subscriptions = subscriptionsRepository.getAll()
            .filter {
                YearMonth.from(DateUtils.Parse.fromDate(it.date)) <= YearMonth.from(today)
                        && (it.endDate == null || DateUtils.Parse.fromDate(it.endDate) >= today)
            }
            .map { it.toItemModel(today, currency) }
            .sortedBy { it.date }

        val header = if (subscriptions.isEmpty()) {
            emptyList()
        } else {
            listOf(HeaderModel(stringsManager.getString(R.string.active_subscriptions)))
        }
        return header.plus(SubscriptionsListModel(subscriptions))
    }

    private suspend fun getCurrentMonthTransactions(): List<WalletAdapterModel> {
        val currentMonth = YearMonth.from(DateUtils.Provide.nowDevice())
        val currency = appPreferences.getAppCurrency()
        return transactionsRepository.getAll()
            .map { DateUtils.Parse.fromDateTime(it.date) to it }
            .filter { YearMonth.from(it.first) == currentMonth }
            .groupBy { LocalDate.from(it.first) }
            .toList()
            .sortedByDescending { it.first }
            .flatMap {
                listOf(HeaderModel(DateUtils.Format.toWeekdayDayMonth(it.first)))
                    .plus(it.second.map { it.second.toItemModel(currency) })
            }
    }

    override fun deleteTransaction(id: Int, completion: () -> Unit) {
        makeRequest(request = {
            transactionsRepository.delete(id)
        }, { completion() })
    }

    override fun updateTotalBudget(newValue: BigDecimal, completion: () -> Unit) {
        makeRequest(request = {
            val monthModel = getCurrentMonthModel()
            monthsRepository.update(monthModel.copy(totalBudget = newValue.toString()))
        }, { completion() })
    }

}