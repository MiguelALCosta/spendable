package com.app.spendable.injection

import android.content.Context
import com.app.spendable.data.ISubscriptionsRepository
import com.app.spendable.data.ITransactionsRepository
import com.app.spendable.data.SubscriptionsRepository
import com.app.spendable.data.TransactionsRepository
import com.app.spendable.data.db.AppDatabase
import com.app.spendable.data.db.IAppDatabase
import com.app.spendable.presentation.add.subscription.AddSubscriptionPresenter
import com.app.spendable.presentation.add.subscription.IAddSubscriptionPresenter
import com.app.spendable.presentation.add.transaction.AddTransactionPresenter
import com.app.spendable.presentation.add.transaction.IAddTransactionPresenter
import com.app.spendable.presentation.main.IMainPresenter
import com.app.spendable.presentation.main.MainPresenter
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
    fun provideAddTransactionPresenter(
        stringsManager: IStringsManager,
        transactionsRepository: ITransactionsRepository
    ): IAddTransactionPresenter {
        return AddTransactionPresenter(stringsManager, transactionsRepository)
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
    fun provideAddSubscriptionPresenter(
        stringsManager: IStringsManager,
        subscriptionsRepository: ISubscriptionsRepository
    ): IAddSubscriptionPresenter {
        return AddSubscriptionPresenter(stringsManager, subscriptionsRepository)
    }
}