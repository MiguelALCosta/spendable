package com.app.spendable.presentation.calendar

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.spendable.presentation.components.HeaderComponent
import com.app.spendable.presentation.components.MessageComponent
import com.app.spendable.presentation.components.MonthCardComponent
import com.app.spendable.utils.setRecyclerViewLayoutParams

class CalendarAdapter(
    val context: Context,
    var models: List<CalendarAdapterModel>,
    val onClick: (CalendarAdapterModel) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private enum class ModelType {
        MONTH_CARD, HEADER, MESSAGE
    }

    override fun getItemCount(): Int {
        return models.count()
    }

    override fun getItemViewType(position: Int): Int {
        return when (models[position]) {
            is CalendarAdapterModel.MonthCard -> ModelType.MONTH_CARD.ordinal
            is CalendarAdapterModel.Header -> ModelType.HEADER.ordinal
            is CalendarAdapterModel.Message -> ModelType.MESSAGE.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (ModelType.entries[viewType]) {
            ModelType.MONTH_CARD -> {
                val component = MonthCardComponent(parent.context).setRecyclerViewLayoutParams()
                MonthCardComponentViewHolder(component)
            }

            ModelType.HEADER -> {
                val component = HeaderComponent(parent.context).setRecyclerViewLayoutParams()
                HeaderComponentViewHolder(component)
            }

            ModelType.MESSAGE -> {
                val component = MessageComponent(parent.context).setRecyclerViewLayoutParams()
                MessageComponentViewHolder(component)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val model = models[position]) {
            is CalendarAdapterModel.MonthCard -> (holder as MonthCardComponentViewHolder).render(
                model,
                onClick
            )

            is CalendarAdapterModel.Header -> (holder as HeaderComponentViewHolder).render(model)
            is CalendarAdapterModel.Message -> (holder as MessageComponentViewHolder).render(model)
        }
    }

    fun updateModels(models: List<CalendarAdapterModel>) {
        this.models = models
        notifyDataSetChanged()
    }

    /** View Holder **/

    private class MonthCardComponentViewHolder(val component: MonthCardComponent) :
        RecyclerView.ViewHolder(component) {
        fun render(model: CalendarAdapterModel.MonthCard, onClick: (CalendarAdapterModel) -> Unit) {
            component.setup(model)
            component.clickableView.setOnClickListener { onClick(model) }
        }
    }

    private class HeaderComponentViewHolder(val component: HeaderComponent) :
        RecyclerView.ViewHolder(component) {
        fun render(model: CalendarAdapterModel.Header) {
            component.setup(model.text)
        }
    }

    private class MessageComponentViewHolder(val component: MessageComponent) :
        RecyclerView.ViewHolder(component) {
        fun render(model: CalendarAdapterModel.Message) {
            component.setup(model.text)
        }
    }

}