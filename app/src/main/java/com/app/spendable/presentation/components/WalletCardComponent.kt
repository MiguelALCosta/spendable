package com.app.spendable.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.app.spendable.databinding.ComponentWalletCardBinding
import com.app.spendable.presentation.wallet.WalletCardModel
import com.app.spendable.utils.toFormatedPrice

class WalletCardComponent(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var binding: ComponentWalletCardBinding

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ComponentWalletCardBinding.inflate(inflater, this, true)
    }

    fun setup(model: WalletCardModel) {
        binding.budget.text = model.budget.toFormatedPrice()
    }

}
