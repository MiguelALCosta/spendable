package com.app.spendable.presentation.common

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.app.spendable.injection.AppPreferencesProvider
import com.app.spendable.injection.StringsManagerProvider
import com.app.spendable.utils.LocaleUtils
import dagger.hilt.android.EntryPointAccessors

abstract class BaseSpendableActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        val appPreferences = EntryPointAccessors
            .fromApplication<AppPreferencesProvider>(newBase.applicationContext)
            .getAppPreferences()
        val updatedContext = LocaleUtils.getContextWithUpdatedLocale(newBase, appPreferences)

        val stringsManager = EntryPointAccessors
            .fromApplication<StringsManagerProvider>(newBase.applicationContext)
            .getStringsManager()
        stringsManager.updateContext(updatedContext)

        super.attachBaseContext(updatedContext)
    }

}