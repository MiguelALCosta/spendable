package com.app.spendable.injection

import android.content.Context
import com.app.spendable.data.IMonthsRepository
import com.app.spendable.data.ISubscriptionsRepository
import com.app.spendable.data.ITransactionsRepository
import com.app.spendable.data.MonthsRepository
import com.app.spendable.data.SubscriptionsRepository
import com.app.spendable.data.TransactionsRepository
import com.app.spendable.data.db.AppDatabase
import com.app.spendable.data.db.IAppDatabase
import com.app.spendable.domain.subscriptionDetail.ISubscriptionDetailInteractor
import com.app.spendable.domain.subscriptionDetail.SubscriptionDetailInteractor
import com.app.spendable.domain.transactionDetail.ITransactionDetailInteractor
import com.app.spendable.domain.transactionDetail.TransactionDetailInteractor
import com.app.spendable.domain.wallet.IWalletInteractor
import com.app.spendable.domain.wallet.WalletInteractor
import com.app.spendable.presentation.main.IMainPresenter
import com.app.spendable.presentation.main.MainPresenter
import com.app.spendable.presentation.subscriptionDetail.ISubscriptionDetailPresenter
import com.app.spendable.presentation.subscriptionDetail.SubscriptionDetailPresenter
import com.app.spendable.presentation.transactionDetail.AddTransactionPresenter
import com.app.spendable.presentation.transactionDetail.IAddTransactionPresenter
import com.app.spendable.presentation.wallet.IWalletPresenter
import com.app.spendable.presentation.wallet.WalletPresenter
import com.app.spendable.utils.IStringsManager
import com.app.spendable.utils.StringsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityComponent::class)
object MainModule {

    @Provides
    fun provideMainPresenter(): IMainPresenter {
        return MainPresenter()
    }

    @Provides
    fun provideTransactionDetailInteractor(
        stringsManager: IStringsManager,
        transactionsRepository: ITransactionsRepository
    ): ITransactionDetailInteractor {
        return TransactionDetailInteractor(stringsManager, transactionsRepository)
    }

    @Provides
    fun provideTransactionDetailPresenter(
        interactor: ITransactionDetailInteractor
    ): IAddTransactionPresenter {
        return AddTransactionPresenter(interactor)
    }

    @Provides
    fun provideStringsManager(@ApplicationContext context: Context): IStringsManager {
        return StringsManager(context)
    }

    @Provides
    fun provideDataBase(@ApplicationContext context: Context): IAppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideTransactionsRepository(database: IAppDatabase): ITransactionsRepository {
        return TransactionsRepository(database)
    }

    @Provides
    fun provideSubscriptionsRepository(database: IAppDatabase): ISubscriptionsRepository {
        return SubscriptionsRepository(database)
    }

    @Provides
    fun provideMonthsRepository(database: IAppDatabase): IMonthsRepository {
        return MonthsRepository(database)
    }

    @Provides
    fun provideSubscriptionDetailInteractor(
        stringsManager: IStringsManager,
        subscriptionsRepository: ISubscriptionsRepository
    ): ISubscriptionDetailInteractor {
        return SubscriptionDetailInteractor(stringsManager, subscriptionsRepository)
    }

    @Provides
    fun provideSubscriptionDetailPresenter(
        interactor: ISubscriptionDetailInteractor
    ): ISubscriptionDetailPresenter {
        return SubscriptionDetailPresenter(interactor)
    }

    @Provides
    fun provideWalletPresenter(
        interactor: IWalletInteractor
    ): IWalletPresenter {
        return WalletPresenter(interactor)
    }

    @Provides
    fun provideWalletInteractor(
        stringsManager: IStringsManager,
        transactionsRepository: ITransactionsRepository,
        subscriptionsRepository: ISubscriptionsRepository,
        monthsRepository: IMonthsRepository
    ): IWalletInteractor {
        return WalletInteractor(
            stringsManager,
            transactionsRepository,
            subscriptionsRepository,
            monthsRepository
        )
    }
}