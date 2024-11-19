package com.app.spendable.presentation.main

import androidx.appcompat.app.AppCompatDelegate
import com.app.spendable.data.preferences.IAppPreferences
import com.app.spendable.domain.settings.AppTheme

interface IMainPresenter {
    fun doStartUpActions()
}

class MainPresenter(private val appPreferences: IAppPreferences) : IMainPresenter {

    override fun doStartUpActions() {
        applyAppTheme()
    }

    private fun applyAppTheme() {
        val theme = appPreferences.getAppTheme()
        val nightMode = when (theme) {
            AppTheme.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            AppTheme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            AppTheme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

}