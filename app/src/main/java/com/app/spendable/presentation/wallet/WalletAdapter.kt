package com.app.spendable.presentation.wallet

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.spendable.presentation.components.WalletCardComponent

class WalletAdapter(val context: Context, val models: List<WalletAdapterModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private enum class ModelType {
        WALLET_CARD
    }

    override fun getItemCount(): Int {
        return models.count()
    }

    override fun getItemViewType(position: Int): Int {
        return when (models[position]) {
            is WalletCardModel -> ModelType.WALLET_CARD.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (ModelType.entries.get(viewType)) {
            ModelType.WALLET_CARD -> CardComponentViewHolder(WalletCardComponent(parent.context))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val model = models[position]) {
            is WalletCardModel -> (holder as CardComponentViewHolder).render(model)
        }
    }

    private class CardComponentViewHolder(val component: WalletCardComponent) :
        RecyclerView.ViewHolder(component) {
        fun render(model: WalletCardModel) {
            component.setup(model)
        }
    }

}