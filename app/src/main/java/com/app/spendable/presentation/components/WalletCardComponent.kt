package com.app.spendable.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
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

    val clickableView: View
        get() = binding.root

    data class SetupConfig(
        val username: String,
        @DrawableRes val avatar: Int,
        val title: String,
        val totalBudget: String,
        val availableBudget: String,
        val percentage: Int
    )

    fun setup(setupConfig: SetupConfig) {
        binding.name.text = setupConfig.username
        binding.avatar.setImageResource(setupConfig.avatar)
        binding.month.text = setupConfig.title.replaceFirstChar(Char::uppercase)
        binding.budget.text = setupConfig.totalBudget
        binding.remaining.text = setupConfig.availableBudget
        binding.progress.progress = setupConfig.percentage
    }

}
