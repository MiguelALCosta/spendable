package com.app.spendable.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.app.spendable.domain.settings.AppCurrency
import com.app.spendable.domain.settings.AppLanguage
import com.app.spendable.domain.settings.AppTheme
import com.app.spendable.utils.toEnum

interface IAppPreferences {
    fun getSystemLanguage(): String
    fun setSystemLanguage(language: String)
    fun getAppLanguage(): AppLanguage
    fun setAppLanguage(language: AppLanguage)
    fun getAppTheme(): AppTheme
    fun setAppTheme(theme: AppTheme)
    fun getAppCurrency(): AppCurrency
    fun setAppCurrency(currency: AppCurrency)
}

class AppPreferences(private val sharedPreferences: SharedPreferences) : IAppPreferences {

    companion object {
        private const val SYSTEM_LANGUAGE_KEY = "SYSTEM_LANGUAGE_KEY"
        private const val APP_LANGUAGE_KEY = "APP_LANGUAGE_KEY"
        private const val APP_THEME_KEY = "APP_THEME_KEY"
        private const val APP_CURRENCY_KEY = "APP_CURRENCY_KEY"
    }

    override fun getSystemLanguage(): String {
        return sharedPreferences.getString(SYSTEM_LANGUAGE_KEY, "EN") ?: "EN"
    }

    override fun setSystemLanguage(language: String) {
        sharedPreferences.edit().putString(SYSTEM_LANGUAGE_KEY, language).apply()
    }

    override fun getAppLanguage(): AppLanguage {
        return sharedPreferences.getString(APP_LANGUAGE_KEY, AppLanguage.SYSTEM.name)
            ?.toEnum<AppLanguage>() ?: AppLanguage.SYSTEM
    }

    override fun setAppLanguage(language: AppLanguage) {
        sharedPreferences.edit().putString(APP_LANGUAGE_KEY, language.name).apply()
    }

    override fun getAppTheme(): AppTheme {
        return sharedPreferences.getString(APP_THEME_KEY, AppTheme.SYSTEM.name)
            ?.toEnum<AppTheme>() ?: AppTheme.SYSTEM
    }

    override fun setAppTheme(theme: AppTheme) {
        sharedPreferences.edit().putString(APP_THEME_KEY, theme.name).apply()
    }

    override fun getAppCurrency(): AppCurrency {
        return sharedPreferences.getString(APP_CURRENCY_KEY, "EUR")
            ?.toEnum<AppCurrency>() ?: AppCurrency.EUR
    }

    override fun setAppCurrency(currency: AppCurrency) {
        sharedPreferences.edit().putString(APP_CURRENCY_KEY, currency.name).apply()
    }
}

fun buildSharedPreferences(context: Context): SharedPreferences {
    return EncryptedSharedPreferences.create(
        "com.app.spendable_encrypted_preferences",
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}