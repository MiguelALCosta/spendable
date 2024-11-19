package com.app.spendable.utils

import android.content.Context
import android.content.res.Configuration
import com.app.spendable.data.preferences.IAppPreferences
import com.app.spendable.domain.settings.AppLanguage
import java.util.Locale

object LocaleUtils {

    private fun getSafeLanguageCode(languageCode: String) =
        if (listOf("PT", "EN").contains(languageCode.uppercase())) {
            languageCode
        } else "EN"

    fun getContextWithUpdatedLocale(context: Context, languageCode: String): Context {
        val safeLanguageCode = getSafeLanguageCode(languageCode)
        val newLocale = Locale(safeLanguageCode)
        val config = Configuration(context.resources.configuration)
        config.setLocale(newLocale)
        Locale.setDefault(newLocale)
        return context.createConfigurationContext(config)
    }

    private fun IAppPreferences.getSafeSystemLanguageCode() =
        getSystemLanguage().let {
            if (listOf("PT", "EN").contains(it.uppercase())) it else "EN"
        }

    private fun IAppPreferences.getSafeLanguageCode() =
        when (val appLanguage = getAppLanguage()) {
            AppLanguage.SYSTEM -> getSafeSystemLanguageCode()
            else -> appLanguage.name
        }

    fun getContextWithUpdatedLocale(context: Context, appPreferences: IAppPreferences): Context {
        val safeLanguageCode = appPreferences.getSafeLanguageCode()
        val newLocale = Locale(safeLanguageCode)
        val config = Configuration(context.resources.configuration)
        config.setLocale(newLocale)
        Locale.setDefault(newLocale)
        return context.createConfigurationContext(config)
    }

}