package com.app.spendable.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import com.app.spendable.databinding.ComponentSubscriptionItemBinding

class SubscriptionItemComponent(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var binding: ComponentSubscriptionItemBinding

    val clickableView: View
        get() = binding.root

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ComponentSubscriptionItemBinding.inflate(inflater, this, true)
    }

    data class SetupConfig(
        @DrawableRes val icon: Int,
        val title: String,
        val cost: String,
        val footer: String
    )

    fun setup(setupConfig: SetupConfig) {
        binding.icon.setImageResource(setupConfig.icon)
        binding.title.text = setupConfig.title
        binding.cost.text = setupConfig.cost
        binding.date.text = setupConfig.footer
    }

}
