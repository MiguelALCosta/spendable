package com.app.spendable.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.app.spendable.databinding.ComponentWalletCardBinding

class WalletCardComponent(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var binding: ComponentWalletCardBinding

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ComponentWalletCardBinding.inflate(inflater, this, true)
    }

    val clickableViews: List<View>
        get() = listOf(binding.editButton, binding.budgetTitle, binding.budget)

    data class SetupConfig(
        val title: String,
        val totalBudget: String,
        val availableBudget: String,
        val percentage: Int,
        val isEditable: Boolean
    )

    fun setup(setupConfig: SetupConfig) {
        binding.month.text = setupConfig.title.replaceFirstChar(Char::uppercase)
        binding.budget.text = setupConfig.totalBudget
        binding.remaining.text = setupConfig.availableBudget
        binding.progress.progress = setupConfig.percentage

        binding.editButton.visibility = if (setupConfig.isEditable) VISIBLE else GONE
    }

}
