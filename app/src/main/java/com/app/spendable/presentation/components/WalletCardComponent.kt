package com.app.spendable.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.app.spendable.databinding.ComponentWalletCardBinding
import com.app.spendable.presentation.wallet.WalletCardModel
import com.app.spendable.utils.DateUtils
import com.app.spendable.utils.toFormatedPrice
import java.math.BigDecimal

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

    fun setup(model: WalletCardModel) {
        binding.month.text =
            DateUtils.Format.toFullMonthYear(model.month).replaceFirstChar(Char::uppercase)
        binding.budget.text = model.budget.toFormatedPrice(model.currency)
        binding.remaining.text = (model.budget - model.spent).toFormatedPrice(model.currency)

        binding.progress.progress =
            ((model.budget - model.spent) * BigDecimal("100") / model.budget).toInt()

    }

}
