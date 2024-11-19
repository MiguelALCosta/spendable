package com.app.spendable.presentation.add

import android.os.Bundle
import com.app.spendable.R
import com.app.spendable.databinding.ActivityAddBinding
import com.app.spendable.presentation.common.BaseSpendableActivity
import com.app.spendable.presentation.common.CloseableView
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddActivity : BaseSpendableActivity(), CloseableView {

    private lateinit var binding: ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPager()
        setupTopBar()
    }

    override fun close() {
        finish()
    }

    private fun setupPager() {
        binding.pager.adapter = AddPagerAdapter(this)

        TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.transaction)
                1 -> tab.text = getString(R.string.subscription)
            }
        }.attach()
    }

    private fun setupTopBar() {
        binding.topBar.icon.setOnClickListener {
            finish()
        }
        binding.topBar.title.text = getString(R.string.title_add)
    }

}
