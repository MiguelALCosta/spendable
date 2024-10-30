package com.app.spendable.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.app.spendable.R
import com.app.spendable.databinding.ActivityMainBinding
import com.app.spendable.presentation.add.AddActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

interface IMainView {
    fun navigateToAdd()
}

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), IMainView {

    @Inject
    lateinit var presenter: IMainPresenter

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )*/
        navView.setupWithNavController(navController)
        supportActionBar?.hide()
        actionBar?.hide()
        presenter.doStuff()
    }

    override fun navigateToAdd() {
        val intent = Intent(this, AddActivity::class.java)
        startActivity(intent)
    }

}
