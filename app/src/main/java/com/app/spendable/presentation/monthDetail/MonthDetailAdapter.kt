package com.app.spendable.presentation.monthDetail

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.spendable.presentation.components.HeaderComponent
import com.app.spendable.presentation.components.MonthBalanceComponent
import com.app.spendable.presentation.components.SubscriptionsListComponent
import com.app.spendable.presentation.components.TransactionItemComponent
import com.app.spendable.presentation.wallet.SubscriptionListItemModel
import com.app.spendable.utils.setRecyclerViewLayoutParams

class MonthDetailAdapter(
    val context: Context,
    var models: List<MonthDetailAdapterModel>,
    val onClick: (Any) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private enum class ModelType {
        BALANCE, HEADER, TRANSACTION_ITEM, SUBSCRIPTION_LIST
    }

    override fun getItemCount(): Int {
        return models.count()
    }

    override fun getItemViewType(position: Int): Int {
        return when (models[position]) {
            is MonthDetailAdapterModel.Balance -> ModelType.BALANCE.ordinal
            is MonthDetailAdapterModel.Header -> ModelType.HEADER.ordinal
            is MonthDetailAdapterModel.Transaction -> ModelType.TRANSACTION_ITEM.ordinal
            is MonthDetailAdapterModel.SubscriptionsList -> ModelType.SUBSCRIPTION_LIST.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (ModelType.entries[viewType]) {
            ModelType.BALANCE -> {
                val component = MonthBalanceComponent(parent.context).setRecyclerViewLayoutParams()
                BalanceComponentViewHolder(component)
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
            is MonthDetailAdapterModel.Balance ->
                (holder as BalanceComponentViewHolder).render(model)

            is MonthDetailAdapterModel.Header ->
                (holder as HeaderComponentViewHolder).render(model)

            is MonthDetailAdapterModel.Transaction ->
                (holder as TransactionItemComponentViewHolder).render(model, onClick)

            is MonthDetailAdapterModel.SubscriptionsList ->
                (holder as SubscriptionsListComponentViewHolder).render(model, onClick)
        }
    }

    fun updateModels(models: List<MonthDetailAdapterModel>) {
        this.models = models
        notifyDataSetChanged()
    }

    /** View Holder **/

    private class BalanceComponentViewHolder(val component: MonthBalanceComponent) :
        RecyclerView.ViewHolder(component) {
        fun render(model: MonthDetailAdapterModel.Balance) {
            component.setup(model.config)
        }
    }

    private class HeaderComponentViewHolder(val component: HeaderComponent) :
        RecyclerView.ViewHolder(component) {
        fun render(model: MonthDetailAdapterModel.Header) {
            component.setup(model.text)
        }
    }

    private class TransactionItemComponentViewHolder(val component: TransactionItemComponent) :
        RecyclerView.ViewHolder(component) {
        fun render(
            model: MonthDetailAdapterModel.Transaction,
            onClick: (MonthDetailAdapterModel) -> Unit
        ) {
            component.setup(model.config)
            component.clickableView.setOnClickListener { onClick(model) }
        }
    }

    private class SubscriptionsListComponentViewHolder(val component: SubscriptionsListComponent) :
        RecyclerView.ViewHolder(component) {
        fun render(
            model: MonthDetailAdapterModel.SubscriptionsList,
            onClick: (SubscriptionListItemModel) -> Unit
        ) {
            component.setup(model.items, onClick)
        }
    }

}