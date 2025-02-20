package com.app.spendable.presentation.components

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.spendable.presentation.wallet.SubscriptionListItemModel

class SubscriptionsListAdapter(
    val context: Context,
    val models: List<SubscriptionListItemModel>,
    val onClick: (SubscriptionListItemModel) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return models.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val component = SubscriptionItemComponent(parent.context)
        return ViewHolder(component)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).render(models[position], onClick)
    }

    private class ViewHolder(val component: SubscriptionItemComponent) :
        RecyclerView.ViewHolder(component) {

        fun render(model: SubscriptionListItemModel, onClick: (SubscriptionListItemModel) -> Unit) {
            component.setup(model.config)
            component.clickableView.setOnClickListener { onClick(model) }
        }
    }

}