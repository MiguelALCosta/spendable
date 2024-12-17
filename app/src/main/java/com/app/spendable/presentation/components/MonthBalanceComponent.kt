package com.app.spendable.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.app.spendable.R
import com.app.spendable.databinding.ComponentMonthBalanceBinding
import com.app.spendable.domain.settings.AppCurrency
import com.app.spendable.utils.IStringsManager
import com.app.spendable.utils.PriceUtils
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import javax.inject.Inject

@AndroidEntryPoint
class MonthBalanceComponent(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var binding: ComponentMonthBalanceBinding

    @Inject
    lateinit var stringsManager: IStringsManager

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ComponentMonthBalanceBinding.inflate(inflater, this, true)
    }

    data class SetupConfig(
        val totalBudget: BigDecimal,
        val availableBudget: BigDecimal,
        val currency: AppCurrency
    )

    fun setup(setupConfig: SetupConfig) {
        binding.balance.text = stringsManager.getString(R.string.x_of_x).format(
            PriceUtils.Format.toPrice(setupConfig.availableBudget, setupConfig.currency),
            PriceUtils.Format.toPrice(setupConfig.totalBudget, setupConfig.currency)
        )
        binding.progress.progress =
            (setupConfig.availableBudget * BigDecimal("100") / setupConfig.totalBudget).toInt()
    }

}
