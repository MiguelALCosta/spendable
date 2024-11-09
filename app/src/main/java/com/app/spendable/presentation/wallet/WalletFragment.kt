package com.app.spendable.presentation.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.spendable.databinding.FragmentWalletBinding
import com.app.spendable.presentation.main.IMainView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

interface WalletView {
    fun updateView(models: List<WalletAdapterModel>)
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

    /*private fun dummyModels() = listOf(
        WalletCardModel(BigDecimal("100.50"), BigDecimal("100.50")),
        HeaderModel("Subscriptions"),
        SubscriptionsListModel(
            listOf(
                SubscriptionItemModel(
                    SubscriptionIcon.NETFLIX,
                    "Netflix",
                    BigDecimal("49.99"),
                    SubscriptionFrequency.MONTHLY
                ),
                SubscriptionItemModel(
                    SubscriptionIcon.NETFLIX,
                    "Spotify",
                    BigDecimal("49.99"),
                    SubscriptionFrequency.MONTHLY
                ),
                SubscriptionItemModel(
                    SubscriptionIcon.NETFLIX,
                    "Spotify",
                    BigDecimal("49.99"),
                    SubscriptionFrequency.WEEKLY
                ),
                SubscriptionItemModel(
                    SubscriptionIcon.NETFLIX,
                    "Spotify",
                    BigDecimal("49.99"),
                    SubscriptionFrequency.BIWEEKLY
                ),
                SubscriptionItemModel(
                    SubscriptionIcon.NETFLIX,
                    "Spotify",
                    BigDecimal("49.99"),
                    SubscriptionFrequency.DAILY
                ),
                SubscriptionItemModel(
                    SubscriptionIcon.NETFLIX,
                    "Spotify",
                    BigDecimal("49.99"),
                    SubscriptionFrequency.DAILY
                ),
            )
        ),
        HeaderModel("October 2024"),
        TransactionItemModel(TransactionType.EAT_OUT, "McD", "Bom", BigDecimal("100.50")),
        TransactionItemModel(TransactionType.EAT_OUT, "McD", "Bom", BigDecimal("100.50")),
        TransactionItemModel(TransactionType.EAT_OUT, "McD", "Bom", BigDecimal("100.50")),
        TransactionItemModel(TransactionType.TRANSPORTS, "Galp", cost = BigDecimal("100.50")),
        TransactionItemModel(TransactionType.TRANSPORTS, "Galp", cost = BigDecimal("100.50")),
        TransactionItemModel(TransactionType.EAT_OUT, "McD", "Bom", BigDecimal("100.50")),
        TransactionItemModel(TransactionType.TRANSPORTS, "Galp", cost = BigDecimal("100.50")),
        TransactionItemModel(TransactionType.EAT_OUT, "McD", "Bom", BigDecimal("100.50")),
        HeaderModel("November 2024"),
        TransactionItemModel(TransactionType.EAT_OUT, "McD", "Bom", BigDecimal("100.50")),
        TransactionItemModel(TransactionType.EAT_OUT, "McD", "Bom", BigDecimal("100.50")),
        TransactionItemModel(TransactionType.EAT_OUT, "McD", "Bom", BigDecimal("100.50")),
        TransactionItemModel(TransactionType.TRANSPORTS, "Galp", cost = BigDecimal("100.50")),
        TransactionItemModel(TransactionType.TRANSPORTS, "Galp", cost = BigDecimal("100.50")),
        TransactionItemModel(TransactionType.EAT_OUT, "McD", "Bom", BigDecimal("100.50")),
        TransactionItemModel(TransactionType.TRANSPORTS, "Galp", cost = BigDecimal("100.50")),
        TransactionItemModel(TransactionType.EAT_OUT, "McD", "Bom", BigDecimal("100.50")),
    )*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this@WalletFragment.adapter = WalletAdapter(context, emptyList())
            adapter = this@WalletFragment.adapter
        }

        binding.addButton.setOnClickListener {
            (activity as? IMainView)?.navigateToAdd()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.refreshWalletInfo()
    }

    override fun updateView(models: List<WalletAdapterModel>) {
        adapter?.updateModels(models)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        presenter.unbind()
    }
}