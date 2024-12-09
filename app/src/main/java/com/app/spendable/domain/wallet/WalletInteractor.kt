package com.app.spendable.domain.wallet

import com.app.spendable.R
import com.app.spendable.data.IMonthsRepository
import com.app.spendable.data.ISubscriptionsRepository
import com.app.spendable.data.ITransactionsRepository
import com.app.spendable.data.preferences.IAppPreferences
import com.app.spendable.domain.BaseInteractor
import com.app.spendable.domain.Month
import com.app.spendable.domain.MonthCreationRequest
import com.app.spendable.domain.TransactionCreationRequest
import com.app.spendable.presentation.components.WalletCardComponent
import com.app.spendable.presentation.wallet.TransactionType
import com.app.spendable.presentation.wallet.WalletAdapterModel
import com.app.spendable.utils.AppConstants
import com.app.spendable.utils.DateUtils
import com.app.spendable.utils.IStringsManager
import com.app.spendable.utils.PriceUtils
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth

interface IWalletInteractor {
    fun getModels(completion: (List<WalletAdapterModel>) -> Unit)
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

            val months = monthsRepository.getAll()
            if (months.firstOrNull { it.date == YearMonth.of(2023, 2) } == null) {
                monthsRepository.create(
                    MonthCreationRequest(
                        date = YearMonth.of(2023, 2),
                        totalBudget = BigDecimal("1000.00"),
                        totalSpent = null
                    )
                )
                transactionsRepository.create(
                    TransactionCreationRequest(
                        type = TransactionType.EAT_OUT,
                        title = "Mackie D",
                        description = "Ganda mac chavalo",
                        cost = BigDecimal("12.30"),
                        date = LocalDateTime.of(LocalDate.of(2024, 10, 12), LocalTime.NOON)
                    )
                )
            }

            getWalletCard()
                .plus(getCurrentMonthSubscriptions())
                .plus(getCurrentMonthTransactions())
        }, completion)
    }

    private suspend fun getWalletCard(): List<WalletAdapterModel> {
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
    }

    private suspend fun getCurrentMonthModel(): Month? {
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
    }

    private suspend fun getCurrentMonthSubscriptions(): List<WalletAdapterModel> {
        val today = DateUtils.Provide.nowDevice().toLocalDate()
        val currency = appPreferences.getAppCurrency()
        val subscriptions = subscriptionsRepository.getAll()
            .filter {
                val payDate = DateUtils.Provide.inCurrentMonth(it.date)
                YearMonth.from(it.date) <= YearMonth.from(today)
                        && (it.endDate == null || it.endDate >= payDate)
            }
            .map { it.toWalletItemModel(stringsManager, today, currency) }
            .sortedBy { it.order }

        val header = if (subscriptions.isEmpty()) {
            emptyList()
        } else {
            listOf(WalletAdapterModel.Header(stringsManager.getString(R.string.subscriptions)))
        }
        return header.plus(WalletAdapterModel.SubscriptionsList(subscriptions))
    }

    private suspend fun getCurrentMonthTransactions(): List<WalletAdapterModel> {
        val currentMonth = YearMonth.from(DateUtils.Provide.nowDevice())
        val currency = appPreferences.getAppCurrency()
        return transactionsRepository.getAll()
            .filter { YearMonth.from(it.date) == currentMonth }
            .groupBy { it.date.toLocalDate() }
            .toList()
            .sortedByDescending { it.first }
            .flatMap { (date, transactions) ->
                listOf(WalletAdapterModel.Header(DateUtils.Format.toWeekdayDayMonth(date)))
                    .plus(transactions.map { it.toWalletAdapterModel(currency) })
            }
            .ifEmpty { listOf(WalletAdapterModel.Message(stringsManager.getString(R.string.no_transactions_message))) }
    }

    override fun updateTotalBudget(newValue: BigDecimal, completion: () -> Unit) {
        makeRequest(request = {
            getCurrentMonthModel()?.let {
                monthsRepository.update(it.copy(totalBudget = newValue))
            }
        }, { completion() })
    }

}