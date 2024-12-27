package com.app.spendable.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.app.spendable.domain.Avatar
import com.app.spendable.domain.settings.AppCurrency
import com.app.spendable.domain.settings.AppLanguage
import com.app.spendable.domain.settings.AppTheme
import com.app.spendable.utils.AppConstants
import com.app.spendable.utils.DateUtils
import com.app.spendable.utils.toEnum
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

interface IAppPreferences {
    fun getSystemLanguage(): String
    fun setSystemLanguage(language: String)
    fun getAppLanguage(): AppLanguage
    fun setAppLanguage(language: AppLanguage)
    fun getAppTheme(): AppTheme
    fun setAppTheme(theme: AppTheme)
    fun getAppCurrency(): AppCurrency
    fun setAppCurrency(currency: AppCurrency)
    fun getUsername(): String
    fun setUsername(username: String)
    fun getUserPoints(): Int
    fun setUserPoints(points: Int)
    fun getUserAvatar(): Avatar
    fun setUserAvatar(avatar: Avatar)
    fun setDailyRewardGiven()
    fun shouldGiveDailyReward(): Boolean
}

class AppPreferences(private val sharedPreferences: SharedPreferences) : IAppPreferences {

    companion object {
        private const val SYSTEM_LANGUAGE_KEY = "SYSTEM_LANGUAGE_KEY"
        private const val APP_LANGUAGE_KEY = "APP_LANGUAGE_KEY"
        private const val APP_THEME_KEY = "APP_THEME_KEY"
        private const val APP_CURRENCY_KEY = "APP_CURRENCY_KEY"
        private const val USERNAME_KEY = "USERNAME_KEY"
        private const val USER_POINTS_KEY = "USER_POINTS_KEY"
        private const val USER_AVATAR_KEY = "USER_AVATAR_KEY"
        private const val DAILY_REWARD_LAST_GIVEN_DATE_KEY = "DAILY_REWARD_LAST_GIVEN_DATE_KEY"
    }

    override fun getSystemLanguage(): String {
        val default = AppConstants.DEFAULT_SYSTEM_LANGUAGE.name
        return sharedPreferences.getString(SYSTEM_LANGUAGE_KEY, default) ?: default
    }

    override fun setSystemLanguage(language: String) {
        sharedPreferences.edit().putString(SYSTEM_LANGUAGE_KEY, language).apply()
    }

    override fun getAppLanguage(): AppLanguage {
        val default = AppConstants.DEFAULT_APP_LANGUAGE
        return sharedPreferences.getString(APP_LANGUAGE_KEY, default.name)
            ?.toEnum<AppLanguage>() ?: default
    }

    override fun setAppLanguage(language: AppLanguage) {
        sharedPreferences.edit().putString(APP_LANGUAGE_KEY, language.name).apply()
    }

    override fun getAppTheme(): AppTheme {
        val default = AppConstants.DEFAULT_APP_THEME
        return sharedPreferences.getString(APP_THEME_KEY, default.name)
            ?.toEnum<AppTheme>() ?: default
    }

    override fun setAppTheme(theme: AppTheme) {
        sharedPreferences.edit().putString(APP_THEME_KEY, theme.name).apply()
    }

    override fun getAppCurrency(): AppCurrency {
        val default = AppConstants.DEFAULT_APP_CURRENCY
        return sharedPreferences.getString(APP_CURRENCY_KEY, default.name)
            ?.toEnum<AppCurrency>() ?: default
    }

    override fun setAppCurrency(currency: AppCurrency) {
        sharedPreferences.edit().putString(APP_CURRENCY_KEY, currency.name).apply()
    }

    override fun getUserPoints(): Int {
        return sharedPreferences.getInt(USER_POINTS_KEY, 0)
    }

    override fun setUserPoints(points: Int) {
        sharedPreferences.edit().putInt(USER_POINTS_KEY, points).apply()
    }

    override fun getUsername(): String {
        return sharedPreferences.getString(USERNAME_KEY, "") ?: ""
    }

    override fun setUsername(username: String) {
        sharedPreferences.edit().putString(USERNAME_KEY, username).apply()
    }

    override fun getUserAvatar(): Avatar {
        return sharedPreferences.getString(USER_AVATAR_KEY, Avatar.BASE.name)
            ?.toEnum<Avatar>() ?: Avatar.BASE
    }

    override fun setUserAvatar(avatar: Avatar) {
        sharedPreferences.edit().putString(USER_AVATAR_KEY, avatar.name).apply()
    }

    override fun setDailyRewardGiven() {
        val millis = DateUtils.Provide.nowUTC().toInstant(ZoneOffset.UTC).toEpochMilli()
        sharedPreferences.edit().putLong(DAILY_REWARD_LAST_GIVEN_DATE_KEY, millis).apply()
    }

    override fun shouldGiveDailyReward(): Boolean {
        val lastGivenDate = try {
            val millis = sharedPreferences.getLong(DAILY_REWARD_LAST_GIVEN_DATE_KEY, 0)
            val deviceTimeZone = DateUtils.Provide.deviceTimeZone()
            Instant.ofEpochMilli(millis).atZone(deviceTimeZone).toLocalDate()
        } catch (_: Throwable) {
            LocalDate.MIN
        }
        val nowDate = DateUtils.Provide.nowDevice().toLocalDate()
        return nowDate > lastGivenDate
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