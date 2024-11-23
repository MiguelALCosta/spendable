package com.app.spendable.injection

import android.content.Context
import android.content.SharedPreferences
import com.app.spendable.data.IMonthsRepository
import com.app.spendable.data.ISubscriptionsRepository
import com.app.spendable.data.ITransactionsRepository
import com.app.spendable.data.MonthsRepository
import com.app.spendable.data.SubscriptionsRepository
import com.app.spendable.data.TransactionsRepository
import com.app.spendable.data.db.AppDatabase
import com.app.spendable.data.db.IAppDatabase
import com.app.spendable.data.preferences.AppPreferences
import com.app.spendable.data.preferences.IAppPreferences
import com.app.spendable.data.preferences.buildSharedPreferences
import com.app.spendable.domain.settings.ISettingsInteractor
import com.app.spendable.domain.settings.SettingsInteractor
import com.app.spendable.domain.subscriptionDetail.ISubscriptionDetailInteractor
import com.app.spendable.domain.subscriptionDetail.SubscriptionDetailInteractor
import com.app.spendable.domain.transactionDetail.ITransactionDetailInteractor
import com.app.spendable.domain.transactionDetail.TransactionDetailInteractor
import com.app.spendable.domain.wallet.IWalletInteractor
import com.app.spendable.domain.wallet.WalletInteractor
import com.app.spendable.presentation.main.IMainPresenter
import com.app.spendable.presentation.main.MainPresenter
import com.app.spendable.presentation.settings.ISettingsPresenter
import com.app.spendable.presentation.settings.SettingsPresenter
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
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object MainModule {

    @Provides
    fun provideMainPresenter(appPreferences: IAppPreferences): IMainPresenter {
        return MainPresenter(appPreferences)
    }

    @Provides
    fun provideTransactionDetailInteractor(
        stringsManager: IStringsManager,
        appPreferences: IAppPreferences,
        transactionsRepository: ITransactionsRepository
    ): ITransactionDetailInteractor {
        return TransactionDetailInteractor(stringsManager, appPreferences, transactionsRepository)
    }

    @Provides
    fun provideTransactionDetailPresenter(
        interactor: ITransactionDetailInteractor
    ): IAddTransactionPresenter {
        return AddTransactionPresenter(interactor)
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
        appPreferences: IAppPreferences,
        subscriptionsRepository: ISubscriptionsRepository
    ): ISubscriptionDetailInteractor {
        return SubscriptionDetailInteractor(stringsManager, appPreferences, subscriptionsRepository)
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
        appPreferences: IAppPreferences,
        transactionsRepository: ITransactionsRepository,
        subscriptionsRepository: ISubscriptionsRepository,
        monthsRepository: IMonthsRepository
    ): IWalletInteractor {
        return WalletInteractor(
            stringsManager,
            appPreferences,
            transactionsRepository,
            subscriptionsRepository,
            monthsRepository
        )
    }

    @Provides
    fun provideSettingsInteractor(
        appPreferences: IAppPreferences,
        transactionsRepository: ITransactionsRepository,
        subscriptionsRepository: ISubscriptionsRepository,
        monthsRepository: IMonthsRepository
    ): ISettingsInteractor {
        return SettingsInteractor(
            appPreferences,
            transactionsRepository,
            subscriptionsRepository,
            monthsRepository
        )
    }

    @Provides
    fun provideSettingsPresenter(
        interactor: ISettingsInteractor,
        stringsManager: IStringsManager
    ): ISettingsPresenter {
        return SettingsPresenter(interactor, stringsManager)
    }
}

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {
    @Provides
    @Singleton
    fun provideStringsManager(@ApplicationContext context: Context): IStringsManager {
        return StringsManager(context)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return buildSharedPreferences(context)
    }

    @Provides
    @Singleton
    fun provideAppPreferences(sharedPreferences: SharedPreferences): IAppPreferences {
        return AppPreferences(sharedPreferences)
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppPreferencesProvider {
    fun getAppPreferences(): IAppPreferences
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface StringsManagerProvider {
    fun getStringsManager(): IStringsManager
}