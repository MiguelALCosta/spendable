package com.app.spendable.domain.calendar

import com.app.spendable.data.IMonthsRepository
import com.app.spendable.data.ISubscriptionsRepository
import com.app.spendable.data.ITransactionsRepository
import com.app.spendable.data.db.Month
import com.app.spendable.data.db.Subscription
import com.app.spendable.data.db.Transaction
import com.app.spendable.data.preferences.IAppPreferences
import com.app.spendable.domain.BaseInteractor
import com.app.spendable.presentation.calendar.CalendarAdapterModel
import com.app.spendable.presentation.calendar.HeaderModel
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
            .associateBy { DateUtils.Parse.fromYearMonth(it.date) }
            .toMutableMap()
        val currentMonth = YearMonth.from(DateUtils.Provide.nowDevice())
        val firstMonth = monthsByDate.minOfOrNull { it.key } ?: currentMonth
        val monthsRange = ChronoUnit.MONTHS.between(firstMonth, currentMonth)

        var transactions: List<Transaction>? = null
        var subscriptions: List<Subscription>? = null
        var totalBudget = "1000.00"

        (0..monthsRange).forEach { i ->
            val yearMonth = firstMonth.plusMonths(i)
            val month = monthsByDate[yearMonth]
            month?.totalBudget?.let { totalBudget = it }
            if (month == null) {
                transactions = transactions ?: transactionsRepository.getAll()
                subscriptions = subscriptions ?: subscriptionsRepository.getAll()
                val newMonth = insertNewMonth(
                    yearMonth, currentMonth, totalBudget, transactions!!, subscriptions!!
                )
                monthsByDate[yearMonth] = newMonth
            } else if (month.totalSpent == null && yearMonth != currentMonth) {
                transactions = transactions ?: transactionsRepository.getAll()
                subscriptions = subscriptions ?: subscriptionsRepository.getAll()
                val updatedMonth = updateMonth(yearMonth, month, transactions!!, subscriptions!!)
                monthsByDate[yearMonth] = updatedMonth
            }
        }
        return monthsByDate.values.toList()
    }

    private suspend fun insertNewMonth(
        yearMonth: YearMonth,
        currentMonth: YearMonth,
        totalBudget: String,
        transactions: List<Transaction>,
        subscriptions: List<Subscription>
    ): Month {
        val totalSpent = if (yearMonth != currentMonth) {
            calculateMonthTotalSpent(yearMonth, transactions, subscriptions)
        } else null
        val month = Month(
            date = DateUtils.Format.toYearMonth(yearMonth),
            totalBudget = totalBudget,
            totalSpent = totalSpent?.toString()
        )
        monthsRepository.insert(month)
        return month
    }

    private suspend fun updateMonth(
        yearMonth: YearMonth,
        month: Month,
        transactions: List<Transaction>,
        subscriptions: List<Subscription>
    ): Month {
        val totalSpent = calculateMonthTotalSpent(yearMonth, transactions, subscriptions)
        val updatedMonth = month.copy(totalSpent = totalSpent.toString())
        monthsRepository.update(updatedMonth)
        return updatedMonth
    }

    private fun calculateMonthTotalSpent(
        yearMonth: YearMonth,
        transactions: List<Transaction>,
        subscriptions: List<Subscription>
    ): BigDecimal {
        val transactionsSum = transactions
            .filter { YearMonth.from(DateUtils.Parse.fromDateTime(it.date)) == yearMonth }
            .sumOf { BigDecimal(it.cost) }

        val subscriptionsSum = subscriptions
            .filter {
                val startDate = DateUtils.Parse.fromDate(it.date)
                val monthPayDate = DateUtils.Provide.inCurrentMonth(startDate)
                val endDate = it.endDate?.let { DateUtils.Parse.fromDate(it) }
                YearMonth.from(startDate) <= yearMonth && (endDate == null || endDate >= monthPayDate)
            }
            .sumOf { BigDecimal(it.cost) }
        return transactionsSum + subscriptionsSum
    }

}