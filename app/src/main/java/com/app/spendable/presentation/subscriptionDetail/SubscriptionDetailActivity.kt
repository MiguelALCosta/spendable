package com.app.spendable.presentation.subscriptionDetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.app.spendable.R
import com.app.spendable.databinding.ActivityCommonWithFragmentBinding
import com.app.spendable.presentation.common.CloseableView
import com.app.spendable.presentation.common.ExtraConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubscriptionDetailActivity : AppCompatActivity(), CloseableView {

    companion object {
        const val SUBSCRIPTION_DETAIL_FRAGMENT_TAG = "SUBSCRIPTION_DETAIL_FRAGMENT_TAG"
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
        binding.topBar.title.text = getString(R.string.subscription)
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
                    SubscriptionDetailFragment(id),
                    SUBSCRIPTION_DETAIL_FRAGMENT_TAG
                )
            }
        }
    }

}
