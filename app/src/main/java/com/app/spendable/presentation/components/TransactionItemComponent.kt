package com.app.spendable.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.app.spendable.R
import com.app.spendable.databinding.ComponentTransactionItemBinding
import com.app.spendable.presentation.wallet.TransactionItemModel
import com.app.spendable.presentation.wallet.TransactionType
import com.app.spendable.utils.toFormatedPrice

class TransactionItemComponent(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var binding: ComponentTransactionItemBinding

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ComponentTransactionItemBinding.inflate(inflater, this, true)
    }

    fun TransactionType.toIcon(): Int {
        return when (this) {
            TransactionType.DUMMY -> R.drawable.ic_wallet
        }
    }


    fun setup(model: TransactionItemModel) {
        binding.icon.setImageResource(model.type.toIcon())
        binding.title.text = model.title

        model.subtitle?.let {
            binding.subtitle.text = it
            binding.subtitle.visibility = VISIBLE
        } ?: run {
            binding.subtitle.visibility = GONE
        }

        binding.cost.text = model.cost.toFormatedPrice()
    }

}
