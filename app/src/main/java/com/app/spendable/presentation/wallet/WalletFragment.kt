package com.app.spendable.presentation.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.spendable.R
import com.app.spendable.databinding.FragmentWalletBinding
import com.app.spendable.domain.DailyReward
import com.app.spendable.presentation.components.RewardPopupComponent
import com.app.spendable.presentation.main.IMainView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

interface WalletView {
    fun updateView(models: List<WalletAdapterModel>)
    fun showDailyRewardPopup(dailyReward: DailyReward)
}

@AndroidEntryPoint
class WalletFragment : Fragment(), WalletView {

    private var _binding: FragmentWalletBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var presenter: IWalletPresenter

    private var adapter: WalletAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWalletBinding.inflate(inflater, container, false)
        presenter.bind(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this@WalletFragment.adapter = WalletAdapter(context, emptyList(), ::onItemClick)
            adapter = this@WalletFragment.adapter
        }

        binding.addButton.setOnClickListener {
            (activity as? IMainView)?.navigateToAdd()
        }
    }

    private fun onItemClick(model: Any) {
        when (model) {
            is WalletAdapterModel.Transaction ->
                (activity as? IMainView)?.showTransactionDetail(model.id)

            is SubscriptionListItemModel ->
                (activity as? IMainView)?.showSubscriptionDetail(model.id)

            is WalletAdapterModel.WalletCard ->
                (activity as? IMainView)?.showUpdateProfileDialog(
                    model.profileState,
                    model.currency,
                    presenter::updateProfile
                )

            else -> {
                // do nothing
            }
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.refreshWalletInfo()
        presenter.requestDailyReward()
    }

    override fun updateView(models: List<WalletAdapterModel>) {
        adapter?.updateModels(models)
    }

    override fun showDailyRewardPopup(dailyReward: DailyReward) {
        binding.rewardPopup.show(
            RewardPopupComponent.SetupConfig(
                initialPoints = dailyReward.initialPoints,
                gainedPoints = dailyReward.gainedPoints,
                title = getString(R.string.daily_reward)
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        presenter.unbind()
    }
}