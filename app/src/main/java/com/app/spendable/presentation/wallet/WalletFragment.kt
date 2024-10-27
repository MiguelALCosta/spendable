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
import java.math.BigDecimal

class WalletFragment : Fragment() {

    private var _binding: FragmentWalletBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun dummyModels() = listOf(
        WalletCardModel(BigDecimal("100.50"), BigDecimal("100.50")),
        HeaderModel("Subscriptions"),
        SubscriptionsListModel(
            listOf(
                SubscriptionItemModel(
                    SubscriptionIcon.DUMMY,
                    "Netflix",
                    BigDecimal("49.99"),
                    SubscriptionRecurrency.MONTHLY
                ),
                SubscriptionItemModel(
                    SubscriptionIcon.DUMMY,
                    "Spotify",
                    BigDecimal("49.99"),
                    SubscriptionRecurrency.MONTHLY
                ),
                SubscriptionItemModel(
                    SubscriptionIcon.DUMMY,
                    "Spotify",
                    BigDecimal("49.99"),
                    SubscriptionRecurrency.WEEKLY
                ),
                SubscriptionItemModel(
                    SubscriptionIcon.DUMMY,
                    "Spotify",
                    BigDecimal("49.99"),
                    SubscriptionRecurrency.BIWEEKLY
                ),
                SubscriptionItemModel(
                    SubscriptionIcon.DUMMY,
                    "Spotify",
                    BigDecimal("49.99"),
                    SubscriptionRecurrency.DAILY
                ),
                SubscriptionItemModel(
                    SubscriptionIcon.DUMMY,
                    "Spotify",
                    BigDecimal("49.99"),
                    SubscriptionRecurrency.DAILY
                ),
            )
        ),
        HeaderModel("October 2024"),
        TransactionItemModel(TransactionType.DUMMY, "McD", "Bom", BigDecimal("100.50")),
        TransactionItemModel(TransactionType.DUMMY, "McD", "Bom", BigDecimal("100.50")),
        TransactionItemModel(TransactionType.DUMMY, "McD", "Bom", BigDecimal("100.50")),
        TransactionItemModel(TransactionType.DUMMY, "Galp", cost = BigDecimal("100.50")),
        TransactionItemModel(TransactionType.DUMMY, "Galp", cost = BigDecimal("100.50")),
        TransactionItemModel(TransactionType.DUMMY, "McD", "Bom", BigDecimal("100.50")),
        TransactionItemModel(TransactionType.DUMMY, "Galp", cost = BigDecimal("100.50")),
        TransactionItemModel(TransactionType.DUMMY, "McD", "Bom", BigDecimal("100.50")),
        HeaderModel("November 2024"),
        TransactionItemModel(TransactionType.DUMMY, "McD", "Bom", BigDecimal("100.50")),
        TransactionItemModel(TransactionType.DUMMY, "McD", "Bom", BigDecimal("100.50")),
        TransactionItemModel(TransactionType.DUMMY, "McD", "Bom", BigDecimal("100.50")),
        TransactionItemModel(TransactionType.DUMMY, "Galp", cost = BigDecimal("100.50")),
        TransactionItemModel(TransactionType.DUMMY, "Galp", cost = BigDecimal("100.50")),
        TransactionItemModel(TransactionType.DUMMY, "McD", "Bom", BigDecimal("100.50")),
        TransactionItemModel(TransactionType.DUMMY, "Galp", cost = BigDecimal("100.50")),
        TransactionItemModel(TransactionType.DUMMY, "McD", "Bom", BigDecimal("100.50")),
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = WalletAdapter(context, dummyModels())
        }

        binding.addButton.setOnClickListener {
            (activity as? IMainView)?.navigateToAdd()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}