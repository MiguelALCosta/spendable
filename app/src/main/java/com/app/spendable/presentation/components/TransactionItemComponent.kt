package com.app.spendable.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import com.app.spendable.databinding.ComponentTransactionItemBinding

class TransactionItemComponent(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var binding: ComponentTransactionItemBinding

    val clickableView: View
        get() = binding.root

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ComponentTransactionItemBinding.inflate(inflater, this, true)
    }

    data class SetupConfig(
        @DrawableRes val icon: Int,
        val title: String,
        val subtitle: String?,
        val cost: String
    )

    fun setup(setupConfig: SetupConfig) {
        binding.icon.setImageResource(setupConfig.icon)
        binding.title.text = setupConfig.title

        if (setupConfig.subtitle.isNullOrBlank()) {
            binding.subtitle.visibility = GONE
        } else {
            binding.subtitle.text = setupConfig.subtitle
            binding.subtitle.visibility = VISIBLE
        }

        binding.cost.text = setupConfig.cost
    }

}
