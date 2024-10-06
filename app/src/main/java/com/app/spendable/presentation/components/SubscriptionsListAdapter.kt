package com.app.spendable.presentation.components

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.spendable.presentation.wallet.SubscriptionItemModel

class SubscriptionsListAdapter(val context: Context, val models: List<SubscriptionItemModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return models.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val component = SubscriptionItemComponent(parent.context)
        return ViewHolder(component)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).render(models[position])
    }

    private class ViewHolder(val component: SubscriptionItemComponent) :
        RecyclerView.ViewHolder(component) {

        fun render(model: SubscriptionItemModel) {
            component.setup(model)
        }
    }

}