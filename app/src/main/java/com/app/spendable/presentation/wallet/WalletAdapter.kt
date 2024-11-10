package com.app.spendable.presentation.wallet

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.spendable.presentation.components.HeaderComponent
import com.app.spendable.presentation.components.SubscriptionsListComponent
import com.app.spendable.presentation.components.TransactionItemComponent
import com.app.spendable.presentation.components.WalletCardComponent
import com.app.spendable.utils.setRecyclerViewLayoutParams

class WalletAdapter(
    val context: Context,
    var models: List<WalletAdapterModel>,
    val onClick: (WalletAdapterModel) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private enum class ModelType {
        WALLET_CARD, HEADER, TRANSACTION_ITEM, SUBSCRIPTION_LIST
    }

    override fun getItemCount(): Int {
        return models.count()
    }

    override fun getItemViewType(position: Int): Int {
        return when (models[position]) {
            is WalletCardModel -> ModelType.WALLET_CARD.ordinal
            is HeaderModel -> ModelType.HEADER.ordinal
            is TransactionItemModel -> ModelType.TRANSACTION_ITEM.ordinal
            is SubscriptionsListModel -> ModelType.SUBSCRIPTION_LIST.ordinal
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

            ModelType.SUBSCRIPTION_LIST -> {
                val component =
                    SubscriptionsListComponent(parent.context).setRecyclerViewLayoutParams()
                SubscriptionsListComponentViewHolder(component)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val model = models[position]) {
            is WalletCardModel -> (holder as CardComponentViewHolder).render(model)
            is HeaderModel -> (holder as HeaderComponentViewHolder).render(model)
            is TransactionItemModel ->
                (holder as TransactionItemComponentViewHolder).render(model, onClick)

            is SubscriptionsListModel ->
                (holder as SubscriptionsListComponentViewHolder).render(model)
        }
    }

    fun updateModels(models: List<WalletAdapterModel>) {
        this.models = models
        notifyDataSetChanged()
    }

    /** View Holder **/

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
        fun render(model: TransactionItemModel, onClick: (WalletAdapterModel) -> Unit) {
            component.setup(model)
            component.setOnClickListener { onClick(model) }
        }
    }

    private class SubscriptionsListComponentViewHolder(val component: SubscriptionsListComponent) :
        RecyclerView.ViewHolder(component) {
        fun render(model: SubscriptionsListModel) {
            component.setup(model.items)
        }
    }

}