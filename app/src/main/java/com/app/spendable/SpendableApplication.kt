package com.app.spendable

import android.app.Application
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.app.spendable.databinding.ActivityMainBinding
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SpendableApplication : Application()