package com.app.spendable.presentation.wallet

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.spendable.presentation.components.HeaderComponent
import com.app.spendable.presentation.components.MessageComponent
import com.app.spendable.presentation.components.SubscriptionsListComponent
import com.app.spendable.presentation.components.TransactionItemComponent
import com.app.spendable.presentation.components.WalletCardComponent
import com.app.spendable.utils.setRecyclerViewLayoutParams

class WalletAdapter(
    val context: Context,
    var models: List<WalletAdapterModel>,
    val onClick: (Any) -> Unit,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private enum class ModelType {
        WALLET_CARD, HEADER, TRANSACTION_ITEM, SUBSCRIPTION_LIST, MESSAGE
    }

    override fun getItemCount(): Int {
        return models.count()
    }

    override fun getItemViewType(position: Int): Int {
        return when (models[position]) {
            is WalletAdapterModel.WalletCard -> ModelType.WALLET_CARD.ordinal
            is WalletAdapterModel.Header -> ModelType.HEADER.ordinal
            is WalletAdapterModel.Transaction -> ModelType.TRANSACTION_ITEM.ordinal
            is WalletAdapterModel.SubscriptionsList -> ModelType.SUBSCRIPTION_LIST.ordinal
            is WalletAdapterModel.Message -> ModelType.MESSAGE.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (ModelType.entries[viewType]) {
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

            ModelType.MESSAGE -> {
                val component =
                    MessageComponent(parent.context).setRecyclerViewLayoutParams()
                MessageComponentViewHolder(component)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val model = models[position]) {
            is WalletAdapterModel.WalletCard ->
                (holder as CardComponentViewHolder).render(model, onClick)

            is WalletAdapterModel.Header ->
                (holder as HeaderComponentViewHolder).render(model)

            is WalletAdapterModel.Transaction ->
                (holder as TransactionItemComponentViewHolder).render(model, onClick)

            is WalletAdapterModel.SubscriptionsList ->
                (holder as SubscriptionsListComponentViewHolder).render(model, onClick)

            is WalletAdapterModel.Message ->
                (holder as MessageComponentViewHolder).render(model)

        }
    }

    fun updateModels(models: List<WalletAdapterModel>) {
        this.models = models
        notifyDataSetChanged()
    }

    /** View Holder **/

    private class CardComponentViewHolder(val component: WalletCardComponent) :
        RecyclerView.ViewHolder(component) {
        fun render(model: WalletAdapterModel.WalletCard, onClick: (WalletAdapterModel) -> Unit) {
            component.setup(model.config)
            component.clickableView.setOnClickListener { onClick(model) }
        }
    }

    private class HeaderComponentViewHolder(val component: HeaderComponent) :
        RecyclerView.ViewHolder(component) {
        fun render(model: WalletAdapterModel.Header) {
            component.setup(model.text)
        }
    }

    private class TransactionItemComponentViewHolder(val component: TransactionItemComponent) :
        RecyclerView.ViewHolder(component) {
        fun render(model: WalletAdapterModel.Transaction, onClick: (WalletAdapterModel) -> Unit) {
            component.setup(model.config)
            component.clickableView.setOnClickListener { onClick(model) }
        }
    }

    private class SubscriptionsListComponentViewHolder(val component: SubscriptionsListComponent) :
        RecyclerView.ViewHolder(component) {
        fun render(
            model: WalletAdapterModel.SubscriptionsList,
            onClick: (SubscriptionListItemModel) -> Unit
        ) {
            component.setup(model.items, onClick)
        }
    }

    private class MessageComponentViewHolder(val component: MessageComponent) :
        RecyclerView.ViewHolder(component) {
        fun render(model: WalletAdapterModel.Message) {
            component.setup(model.text)
        }
    }

}