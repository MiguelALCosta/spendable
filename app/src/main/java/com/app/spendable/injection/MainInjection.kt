package com.app.spendable.injection

import com.app.spendable.presentation.main.IMainPresenter
import com.app.spendable.presentation.main.MainPresenter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object MainModule {

    @Provides
    fun provideMainPresenter(): IMainPresenter {
        return MainPresenter()
    }
}