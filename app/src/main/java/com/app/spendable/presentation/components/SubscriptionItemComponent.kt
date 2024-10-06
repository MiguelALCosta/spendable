package com.app.spendable.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.app.spendable.databinding.ComponentSubscriptionItemBinding
import com.app.spendable.presentation.toIconResource
import com.app.spendable.presentation.toStringResource
import com.app.spendable.presentation.wallet.SubscriptionItemModel
import com.app.spendable.utils.toFormatedPrice

class SubscriptionItemComponent(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var binding: ComponentSubscriptionItemBinding

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ComponentSubscriptionItemBinding.inflate(inflater, this, true)
    }

    fun setup(model: SubscriptionItemModel) {
        binding.icon.setImageResource(model.iconType.toIconResource())
        binding.title.text = model.title
        binding.cost.text = model.cost.toFormatedPrice()
        binding.recurrency.text = context.getString(model.recurrency.toStringResource())
    }

}
