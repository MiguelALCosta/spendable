package com.app.spendable.presentation.add

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.spendable.presentation.subscriptionDetail.SubscriptionDetailFragment
import com.app.spendable.presentation.transactionDetail.AddTransactionFragment

class AddPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AddTransactionFragment()
            1 -> SubscriptionDetailFragment()
            else -> throw RuntimeException()
        }
    }

}