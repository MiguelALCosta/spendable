package com.app.spendable.utils

import com.app.spendable.domain.settings.AppCurrency
import com.app.spendable.domain.settings.AppLanguage
import com.app.spendable.domain.settings.AppTheme
import java.math.BigDecimal

object AppConstants {
    val DEFAULT_MONTHLY_BUDGET = BigDecimal("1000.00")
    val DEFAULT_SYSTEM_LANGUAGE = AppLanguage.EN
    val DEFAULT_APP_LANGUAGE = AppLanguage.SYSTEM
    val DEFAULT_APP_THEME = AppTheme.SYSTEM
    val DEFAULT_APP_CURRENCY = AppCurrency.EUR
    val DAILY_REWARD_POINTS = 10
}