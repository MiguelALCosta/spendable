package com.app.spendable.presentation.wallet

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.spendable.presentation.components.HeaderComponent
import com.app.spendable.presentation.components.TransactionItemComponent
import com.app.spendable.presentation.components.WalletCardComponent
import com.app.spendable.utils.setRecyclerViewLayoutParams

class WalletAdapter(val context: Context, val models: List<WalletAdapterModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private enum class ModelType {
        WALLET_CARD, HEADER, TRANSACTION_ITEM
    }

    override fun getItemCount(): Int {
        return models.count()
    }

    override fun getItemViewType(position: Int): Int {
        return when (models[position]) {
            is WalletCardModel -> ModelType.WALLET_CARD.ordinal
            is HeaderModel -> ModelType.HEADER.ordinal
            is TransactionItemModel -> ModelType.TRANSACTION_ITEM.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (ModelType.entries.get(viewType)) {
            ModelType.WALLET_CARD -> {
                val component = WalletCardComponent(parent.context).setRecyclerViewLayoutParams()
                CardComponentViewHolder(component)
            }

            ModelType.HEADER -> {
                val component = HeaderComponent(parent.context).setRecyclerViewLayoutParams()
                HeaderComponentViewHolder(component)
            }

            ModelType.TRANSACTION_ITEM -> {
                val component =
                    TransactionItemComponent(parent.context).setRecyclerViewLayoutParams()
                TransactionItemComponentViewHolder(component)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val model = models[position]) {
            is WalletCardModel -> (holder as CardComponentViewHolder).render(model)
            is HeaderModel -> (holder as HeaderComponentViewHolder).render(model)
            is TransactionItemModel -> (holder as TransactionItemComponentViewHolder).render(model)
        }
    }

    private class CardComponentViewHolder(val component: WalletCardComponent) :
        RecyclerView.ViewHolder(component) {
        fun render(model: WalletCardModel) {
            component.setup(model)
        }
    }

    private class HeaderComponentViewHolder(val component: HeaderComponent) :
        RecyclerView.ViewHolder(component) {
        fun render(model: HeaderModel) {
            component.setup(model.text)
        }
    }

    private class TransactionItemComponentViewHolder(val component: TransactionItemComponent) :
        RecyclerView.ViewHolder(component) {
        fun render(model: TransactionItemModel) {
            component.setup(model)
        }
    }

}