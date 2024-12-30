package com.app.spendable.domain.wallet

import com.app.spendable.R
import com.app.spendable.data.IMonthsRepository
import com.app.spendable.data.ISubscriptionsRepository
import com.app.spendable.data.ITransactionsRepository
import com.app.spendable.data.preferences.IAppPreferences
import com.app.spendable.domain.BaseInteractor
import com.app.spendable.domain.DailyReward
import com.app.spendable.domain.Month
import com.app.spendable.domain.MonthCreationRequest
import com.app.spendable.presentation.components.UpdateProfileDialog
import com.app.spendable.presentation.components.WalletCardComponent
import com.app.spendable.presentation.wallet.WalletAdapterModel
import com.app.spendable.utils.AppConstants
import com.app.spendable.utils.DateUtils
import com.app.spendable.utils.IStringsManager
import com.app.spendable.utils.PriceUtils
import java.math.BigDecimal
import java.time.YearMonth

interface IWalletInteractor {
    fun getModels(completion: (List<WalletAdapterModel>) -> Unit)
    fun requestDailyReward(): DailyReward?
    fun updateProfile(profileStateUpdate: UpdateProfileDialog.StateUpdate, completion: () -> Unit)
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
                        type = TransactionType.TRANSPORTS,
                        title = "Bilhete de metro",
                        description = null,
                        cost = BigDecimal("1.80"),
                        date = LocalDateTime.of(LocalDate.of(2024, 10, 29), LocalTime.of(9, 30))
                    )
                )
                transactionsRepository.create(
                    TransactionCreationRequest(
                        type = TransactionType.PET,
                        title = "Consutla de veterinário",
                        description = "Vacinas Bobi",
                        cost = BigDecimal("40.00"),
                        date = LocalDateTime.of(LocalDate.of(2024, 10, 30), LocalTime.of(16, 15))
                    )
                )
                transactionsRepository.create(
                    TransactionCreationRequest(
                        type = TransactionType.HEALTH,
                        title = "Farmácia",
                        description = "Brufen",
                        cost = BigDecimal("10.40"),
                        date = LocalDateTime.of(LocalDate.of(2024, 10, 29), LocalTime.of(18, 23))
                    )
                )
                transactionsRepository.create(
                    TransactionCreationRequest(
                        type = TransactionType.TRANSPORTS,
                        title = "Bilhete de metro",
                        description = null,
                        cost = BigDecimal("1.80"),
                        date = LocalDateTime.of(LocalDate.of(2024, 10, 28), LocalTime.of(9, 30))
                    )
                )
                subscriptionsRepository.create(
                    SubscriptionCreationRequest(
                        category = SubscriptionCategory.MUSIC,
                        iconType = SubscriptionIcon.AUDIBLE,
                        title = "Audible",
                        cost = BigDecimal("33.00"),
                        date = LocalDate.of(2024, 8, 12),
                        frequency = SubscriptionFrequency.MONTHLY
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

        val transactionsSum = transactionsRepository.getByMonth(currentMonth).sumOf { it.cost }

        val subscriptionsSum = subscriptionsRepository.getAllActiveInMonth(currentMonth)
            .filter { DateUtils.Provide.inCurrentMonth(it.date) <= today }
            .sumOf { it.cost }

        val username = appPreferences.getUsername()
            .ifEmpty { stringsManager.getString(R.string.default_username) }
        val avatar = appPreferences.getUserAvatar()
        val appCurrency = appPreferences.getAppCurrency()
        val totalBudget = getCurrentMonthModel()?.totalBudget ?: BigDecimal("0.00")
        val availableBudget = totalBudget - transactionsSum - subscriptionsSum
        val profileState = UpdateProfileDialog.State(
            totalBudget = totalBudget,
            username = username,
            avatar = avatar,
            points = appPreferences.getUserPoints()
        )
        val config = WalletCardComponent.SetupConfig(
            username = username,
            avatar = avatar.drawableRes,
            title = DateUtils.Format.toFullMonthYear(currentMonth),
            totalBudget = PriceUtils.Format.toPrice(totalBudget, appCurrency),
            availableBudget = PriceUtils.Format.toPrice(availableBudget, appCurrency),
            percentage = (availableBudget * BigDecimal("100") / totalBudget).toInt()
        )

        return listOf(WalletAdapterModel.WalletCard(profileState, appCurrency, config))
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
        val subscriptions = subscriptionsRepository.getAllActiveInMonth(YearMonth.from(today))
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
        return transactionsRepository.getByMonth(currentMonth)
            .groupBy { it.date.toLocalDate() }
            .toList()
            .sortedByDescending { it.first }
            .flatMap { (date, transactions) ->
                listOf(WalletAdapterModel.Header(DateUtils.Format.toWeekdayDayMonth(date)))
                    .plus(transactions.map { it.toWalletAdapterModel(currency) })
            }
            .ifEmpty { listOf(WalletAdapterModel.Message(stringsManager.getString(R.string.no_transactions_message))) }
    }

    override fun requestDailyReward(): DailyReward? {
        return if (appPreferences.shouldGiveDailyReward()) {
            val currentPoints = appPreferences.getUserPoints()
            val gainedPoints = AppConstants.DAILY_REWARD_POINTS
            appPreferences.setUserPoints(currentPoints + gainedPoints)
            appPreferences.setDailyRewardGiven()
            DailyReward(currentPoints, gainedPoints)
        } else {
            null
        }
    }

    override fun updateProfile(
        profileStateUpdate: UpdateProfileDialog.StateUpdate,
        completion: () -> Unit
    ) {
        makeRequest(request = {
            getCurrentMonthModel()?.let {
                monthsRepository.update(it.copy(totalBudget = profileStateUpdate.totalBudget))
            }
            appPreferences.setUsername(profileStateUpdate.username)
            appPreferences.setUserAvatar(profileStateUpdate.avatar)
        }, { completion() })
    }

}