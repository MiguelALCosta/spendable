package com.app.spendable

import android.app.Application
import com.app.spendable.data.preferences.IAppPreferences
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale
import javax.inject.Inject

@HiltAndroidApp
class SpendableApplication : Application() {

    @Inject
    lateinit var appPreferences: IAppPreferences

    override fun onCreate() {
        super.onCreate()

        appPreferences.setSystemLanguage(Locale.getDefault().language)
    }

}