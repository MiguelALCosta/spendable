package com.app.spendable.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.app.spendable.R
import com.app.spendable.databinding.ActivityMainBinding
import com.app.spendable.domain.settings.AppCurrency
import com.app.spendable.presentation.add.AddActivity
import com.app.spendable.presentation.common.BaseSpendableActivity
import com.app.spendable.presentation.common.ExtraConstants
import com.app.spendable.presentation.components.UpdateTotalBudgetDialog
import com.app.spendable.presentation.subscriptionDetail.SubscriptionDetailActivity
import com.app.spendable.presentation.transactionDetail.TransactionDetailActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import javax.inject.Inject

interface IMainView {
    fun navigateToAdd()
    fun showTransactionDetail(id: Int)
    fun showSubscriptionDetail(id: Int)
    fun showUpdateTotalBudgetDialog(
        currentValue: BigDecimal,
        currency: AppCurrency,
        onUpdate: (BigDecimal) -> Unit
    )
}

@AndroidEntryPoint
class MainActivity : BaseSpendableActivity(), IMainView {

    companion object {
        private const val UPDATE_TOTAL_BUDGET_DIALOG_TAG = "UPDATE_TOTAL_BUDGET_DIALOG"
    }

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
        presenter.doStartUpActions()
    }

    /*override fun attachBaseContext(newBase: Context) {
        val appPreferences = EntryPointAccessors
            .fromApplication<AppPreferencesProvider>(newBase.applicationContext)
            .getAppPreferences()
        val updatedContext = LocaleUtils.getContextWithUpdatedLocale(newBase, appPreferences)

        val stringsManager = EntryPointAccessors
            .fromApplication<StringsManagerProvider>(newBase.applicationContext)
            .getStringsManager()
        stringsManager.updateContext(updatedContext)

        super.attachBaseContext(updatedContext)
    }*/

    override fun navigateToAdd() {
        val intent = Intent(this, AddActivity::class.java)
        startActivity(intent)
    }

    override fun showTransactionDetail(id: Int) {
        val intent = Intent(this, TransactionDetailActivity::class.java)
        intent.putExtra(ExtraConstants.ID, id)
        startActivity(intent)
    }

    override fun showSubscriptionDetail(id: Int) {
        val intent = Intent(this, SubscriptionDetailActivity::class.java)
        intent.putExtra(ExtraConstants.ID, id)
        startActivity(intent)
    }

    override fun showUpdateTotalBudgetDialog(
        currentValue: BigDecimal,
        currency: AppCurrency,
        onUpdate: (BigDecimal) -> Unit
    ) {
        UpdateTotalBudgetDialog.build(currentValue, currency, onUpdate)
            .show(supportFragmentManager, UPDATE_TOTAL_BUDGET_DIALOG_TAG)
    }

}
