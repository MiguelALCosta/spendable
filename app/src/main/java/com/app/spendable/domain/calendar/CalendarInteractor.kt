package com.app.spendable.domain.calendar

import com.app.spendable.data.IMonthsRepository
import com.app.spendable.data.ISubscriptionsRepository
import com.app.spendable.data.ITransactionsRepository
import com.app.spendable.data.preferences.IAppPreferences
import com.app.spendable.domain.BaseInteractor
import com.app.spendable.domain.Month
import com.app.spendable.domain.MonthCreationRequest
import com.app.spendable.domain.Subscription
import com.app.spendable.domain.Transaction
import com.app.spendable.presentation.calendar.CalendarAdapterModel
import com.app.spendable.presentation.calendar.HeaderModel
import com.app.spendable.utils.AppConstants
import com.app.spendable.utils.DateUtils
import com.app.spendable.utils.IStringsManager
import java.math.BigDecimal
import java.time.YearMonth
import java.time.temporal.ChronoUnit

interface ICalendarInteractor {
    fun getModels(completion: (List<CalendarAdapterModel>) -> Unit)
}

class CalendarInteractor(
    private val stringsManager: IStringsManager,
    private val appPreferences: IAppPreferences,
    private val transactionsRepository: ITransactionsRepository,
    private val subscriptionsRepository: ISubscriptionsRepository,
    private val monthsRepository: IMonthsRepository
) : BaseInteractor(), ICalendarInteractor {

    override fun getModels(completion: (List<CalendarAdapterModel>) -> Unit) {
        makeRequest(request = {
            val currency = appPreferences.getAppCurrency()
            val currentMonth = YearMonth.from(DateUtils.Provide.nowDevice())
            getMonths()
                .mapNotNull { it ->
                    it.toAdapterModel(currency)
                        .let { if (it.month == currentMonth) null else it }
                }
                .sortedByDescending { it.month }
                .groupBy { it.month.year }
                .toList()
                .flatMap { listOf(HeaderModel(it.first.toString())).plus(it.second) }
        }, completion)
    }

    private suspend fun getMonths(): List<Month> {
        val monthsByDate = monthsRepository.getAll()
            .associateBy { it.date }
        val currentMonth = YearMonth.from(DateUtils.Provide.nowDevice())
        val firstMonth = monthsByDate.minOfOrNull { it.key } ?: currentMonth
        val monthsRange = ChronoUnit.MONTHS.between(firstMonth, currentMonth)

        var transactions: List<Transaction>? = null
        var subscriptions: List<Subscription>? = null
        var totalBudget = AppConstants.DEFAULT_MONTHLY_BUDGET
        var hasChanges = false

        (0..monthsRange).forEach { i ->
            val yearMonth = firstMonth.plusMonths(i)
            val month = monthsByDate[yearMonth]
            month?.totalBudget?.let { totalBudget = it }
            if (month == null) {
                transactions = transactions ?: transactionsRepository.getAll()
                subscriptions = subscriptions ?: subscriptionsRepository.getAll()
                createMonth(yearMonth, currentMonth, totalBudget, transactions!!, subscriptions!!)
                hasChanges = true
            } else if (month.totalSpent == null && yearMonth != currentMonth) {
                transactions = transactions ?: transactionsRepository.getAll()
                subscriptions = subscriptions ?: subscriptionsRepository.getAll()
                updateMonth(yearMonth, month, transactions!!, subscriptions!!)
                hasChanges = true
            }
        }
        return if (hasChanges) monthsRepository.getAll() else monthsByDate.values.toList()
    }

    private suspend fun createMonth(
        yearMonth: YearMonth,
        currentMonth: YearMonth,
        totalBudget: BigDecimal,
        transactions: List<Transaction>,
        subscriptions: List<Subscription>
    ) {
        val totalSpent = if (yearMonth != currentMonth) {
            calculateMonthTotalSpent(yearMonth, transactions, subscriptions)
        } else null
        val request = MonthCreationRequest(
            date = yearMonth,
            totalBudget = totalBudget,
            totalSpent = totalSpent
        )
        monthsRepository.create(request)
    }

    private suspend fun updateMonth(
        yearMonth: YearMonth,
        month: Month,
        transactions: List<Transaction>,
        subscriptions: List<Subscription>
    ) {
        val totalSpent = calculateMonthTotalSpent(yearMonth, transactions, subscriptions)
        val updatedMonth = month.copy(totalSpent = totalSpent)
        monthsRepository.update(updatedMonth)
    }

    private fun calculateMonthTotalSpent(
        yearMonth: YearMonth,
        transactions: List<Transaction>,
        subscriptions: List<Subscription>
    ): BigDecimal {
        val transactionsSum = transactions
            .filter { YearMonth.from(it.date) == yearMonth }
            .sumOf { it.cost }

        val subscriptionsSum = subscriptions
            .filter {
                val payDate = DateUtils.Provide.inCurrentMonth(it.date)
                YearMonth.from(it.date) <= yearMonth && (it.endDate == null || it.endDate >= payDate)
            }
            .sumOf { it.cost }
        return transactionsSum + subscriptionsSum
    }

}