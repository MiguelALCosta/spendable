package com.app.spendable.presentation.transactionDetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.app.spendable.R
import com.app.spendable.databinding.ActivityCommonWithFragmentBinding
import com.app.spendable.presentation.common.CloseableView
import com.app.spendable.presentation.common.ExtraConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionDetailActivity : AppCompatActivity(), CloseableView {

    companion object {
        const val TRANSACTION_DETAIL_FRAGMENT_TAG = "TRANSACTION_DETAIL_FRAGMENT_TAG"
    }

    private lateinit var binding: ActivityCommonWithFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCommonWithFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTopBar()
        setupSubscriptionFragment(savedInstanceState)
    }

    override fun close() {
        finish()
    }

    private fun setupTopBar() {
        binding.topBar.icon.setOnClickListener {
            finish()
        }
        binding.topBar.title.text = getString(R.string.transaction)
    }

    private fun setupSubscriptionFragment(savedInstanceState: Bundle?) {
        val id = intent.extras?.getInt(ExtraConstants.ID) ?: run {
            finish()
            return
        }

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(
                    R.id.fragment_container_view,
                    AddTransactionFragment(id),
                    TRANSACTION_DETAIL_FRAGMENT_TAG
                )
            }
        }
    }

}
