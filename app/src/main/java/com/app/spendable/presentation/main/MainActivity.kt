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
import com.app.spendable.presentation.components.UpdateProfileDialog
import com.app.spendable.presentation.monthDetail.MonthDetailActivity
import com.app.spendable.presentation.subscriptionDetail.SubscriptionDetailActivity
import com.app.spendable.presentation.transactionDetail.TransactionDetailActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.time.YearMonth
import javax.inject.Inject

interface IMainView {
    fun navigateToAdd()
    fun showTransactionDetail(id: Int)
    fun showSubscriptionDetail(id: Int)
    fun showUpdateProfileDialog(
        state: UpdateProfileDialog.State,
        currency: AppCurrency,
        onUpdate: (UpdateProfileDialog.StateUpdate) -> Unit
    )

    fun showMonthDetail(month: YearMonth)
}

@AndroidEntryPoint
class MainActivity : BaseSpendableActivity(), IMainView {

    companion object {
        private const val UPDATE_TOTAL_BUDGET_DIALOG_TAG = "UPDATE_TOTAL_BUDGET_DIALOG"
    }

    @Inject
    lateinit var presenter: IMainPresenter

    private lateinit var binding: ActivityMainBinding

    private var profileDialog: UpdateProfileDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
        supportActionBar?.hide()
        actionBar?.hide()
        presenter.doStartUpActions()
    }

    override fun onResume() {
        super.onResume()

        if (profileDialog?.dialog?.isShowing == true) {
            // close profile popup on resume to not overlap daily reward popup
            profileDialog?.dismiss()
        }
    }

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

    override fun showUpdateProfileDialog(
        state: UpdateProfileDialog.State,
        currency: AppCurrency,
        onUpdate: (UpdateProfileDialog.StateUpdate) -> Unit
    ) {
        profileDialog = UpdateProfileDialog.build(state, currency, onUpdate) {
            profileDialog = null
        }
        profileDialog?.show(supportFragmentManager, UPDATE_TOTAL_BUDGET_DIALOG_TAG)
    }

    override fun showMonthDetail(month: YearMonth) {
        val intent = Intent(this, MonthDetailActivity::class.java)
        intent.putExtra(ExtraConstants.MONTH, month)
        startActivity(intent)
    }

}
