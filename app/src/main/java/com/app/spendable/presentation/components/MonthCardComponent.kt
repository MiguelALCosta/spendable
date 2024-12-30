package com.app.spendable.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.app.spendable.R
import com.app.spendable.databinding.ComponentMonthCardBinding
import com.app.spendable.presentation.calendar.CalendarAdapterModel
import com.app.spendable.utils.DateUtils
import com.app.spendable.utils.PriceUtils

class MonthCardComponent(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var binding: ComponentMonthCardBinding

    val clickableView: View
        get() = binding.root

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ComponentMonthCardBinding.inflate(inflater, this, true)
    }

    fun setup(model: CalendarAdapterModel.MonthCard) {
        binding.title.text =
            DateUtils.Format.toFullMonthYear(model.month).replaceFirstChar(Char::uppercase)

        val remainingText = PriceUtils.Format.toPrice(model.budget - model.spent, model.currency)
        val totalText = PriceUtils.Format.toPrice(model.budget, model.currency)
        binding.balance.text = context.getString(R.string.x_of_x).format(remainingText, totalText)
    }

}
