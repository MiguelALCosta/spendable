package com.app.spendable.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.app.spendable.R
import com.app.spendable.databinding.ComponentSubscriptionItemBinding
import com.app.spendable.presentation.toIconResource
import com.app.spendable.presentation.wallet.SubscriptionItemModel
import com.app.spendable.utils.DateUtils
import com.app.spendable.utils.toFormatedPrice
import java.time.temporal.ChronoUnit
import kotlin.math.abs

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
        binding.cost.text = model.cost.toFormatedPrice(model.currency)

        val today = DateUtils.Provide.nowDevice().toLocalDate()
        val daysLeft = ChronoUnit.DAYS.between(today, model.date).toInt()
        binding.date.text = when {
            daysLeft == 0 -> context.getString(R.string.today)
            daysLeft == -1 -> context.getString(R.string.yesterday)
            daysLeft == 1 -> context.getString(R.string.tomorrow)
            daysLeft < -1 -> context.getString(R.string.x_days_ago).format(abs(daysLeft))
            else -> context.getString(R.string.in_x_days).format(daysLeft)
        }
    }

}
