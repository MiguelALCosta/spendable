package com.app.spendable.domain.settings

import androidx.appcompat.app.AppCompatDelegate
import com.app.spendable.data.IMonthsRepository
import com.app.spendable.data.ISubscriptionsRepository
import com.app.spendable.data.ITransactionsRepository
import com.app.spendable.data.preferences.IAppPreferences
import com.app.spendable.domain.BaseInteractor

interface ISettingsInteractor {
    fun getAvailableLanguages(): List<AppLanguage>
    fun getSelectedLanguage(): AppLanguage
    fun getAvailableThemes(): List<AppTheme>
    fun getSelectedTheme(): AppTheme
    fun updateAppLanguage(language: AppLanguage)
    fun updateAppTheme(theme: AppTheme)
    fun clearAppData(completion: (Unit) -> Unit)
}

class SettingsInteractor(
    private val appPreferences: IAppPreferences,
    private val transactionsRepository: ITransactionsRepository,
    private val subscriptionsRepository: ISubscriptionsRepository,
    private val monthsRepository: IMonthsRepository
) : BaseInteractor(), ISettingsInteractor {
    override fun getAvailableLanguages(): List<AppLanguage> {
        return AppLanguage.entries
    }

    override fun getSelectedLanguage(): AppLanguage {
        return appPreferences.getAppLanguage()
    }

    override fun getAvailableThemes(): List<AppTheme> {
        return AppTheme.entries
    }

    override fun getSelectedTheme(): AppTheme {
        return appPreferences.getAppTheme()
    }

    override fun clearAppData(completion: (Unit) -> Unit) {
        makeRequest(request = {
            transactionsRepository.deleteAll()
            subscriptionsRepository.deleteAll()
            monthsRepository.deleteAll()
        }, completion)
    }

    override fun updateAppLanguage(language: AppLanguage) {
        appPreferences.setAppLanguage(language)
    }

    override fun updateAppTheme(theme: AppTheme) {
        appPreferences.setAppTheme(theme)
        val nightMode = when (theme) {
            AppTheme.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            AppTheme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            AppTheme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

}