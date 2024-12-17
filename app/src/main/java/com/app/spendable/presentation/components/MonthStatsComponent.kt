package com.app.spendable.presentation.components

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.icu.text.DecimalFormat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.app.spendable.R
import com.app.spendable.databinding.ComponentMonthStatsBinding
import com.app.spendable.domain.settings.AppCurrency
import com.app.spendable.utils.IStringsManager
import com.app.spendable.utils.getColorFromAttr
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import javax.inject.Inject

@AndroidEntryPoint
class MonthStatsComponent(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private class PercentFormatter : ValueFormatter() {
        private val df = DecimalFormat("##.#")

        override fun getPieLabel(value: Float, pieEntry: PieEntry?): String {
            return df.format(value) + "%"
        }
    }

    private var binding: ComponentMonthStatsBinding

    @Inject
    lateinit var stringsManager: IStringsManager

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ComponentMonthStatsBinding.inflate(inflater, this)


        binding.chart.apply {
            setUsePercentValues(true)
            description.isEnabled = false
            //setExtraOffsets(5, 10, 5, 5)
            setCenterTextTypeface(Typeface.DEFAULT_BOLD)
            setCenterTextSize(14f)
            setCenterTextColor(context.getColorFromAttr(com.google.android.material.R.attr.colorOnSurface))
            setDrawCenterText(true)
            setHoleColor(Color.TRANSPARENT)
            centerText = stringsManager.getString(R.string.total_budget) + "\n1000.00€"

            //setDrawHoleEnabled(true)
            //setTransparentCircleColor(Color.WHITE)
            ///setTransparentCircleAlpha(110)

            holeRadius = 70f
            transparentCircleRadius = 70f

            //setRotationAngle(0)

            // enable rotation of the chart by touch
            setDragDecelerationFrictionCoef(0.96f)
            isRotationEnabled = true
            isHighlightPerTapEnabled = false

            // setUnit(" €");
            // setDrawUnitsInChart(true);

            // add a selection listener
            //setOnChartValueSelectedListener(this)

            animateY(700, Easing.EaseInOutQuad)

            spin(700, 180f, 270f, Easing.EaseInOutQuad);
            legend.apply {
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                orientation = Legend.LegendOrientation.VERTICAL
                setDrawInside(false)
                xEntrySpace = 7f
                yEntrySpace = 0f
                yOffset = 0f
                form = Legend.LegendForm.CIRCLE
            }


            // entry label styling
            setEntryLabelColor(Color.WHITE)
            //setEntryLabelTypeface(tfRegular)
            setEntryLabelTextSize(12f)
        }


        /*val entries = listOf(PieEntry(20f, "a"), PieEntry(30f, "b"), PieEntry(50f, "c"))
        val dataset = PieDataSet(entries, "")
        dataset.valueFormatter = PercentFormatter()
        dataset.colors = ColorTemplate.COLORFUL_COLORS.toList()
        dataset.setAutomaticallyDisableSliceSpacing(true)
        dataset.sliceSpace = 5f
        binding.chart.data = PieData(dataset)
        binding.chart.invalidate()*/
    }

    enum class ExpenseEntryType {
        EAT_OUT, MARKET, SHOPPING, TRANSPORTS, ENTERTAINMENT, BILLS, HOLIDAYS, HEALTH, EDUCATION, PET,
        KIDS, SUBSCRIPTIONS, OTHER
    }

    data class ExpenseEntry(
        val cost: BigDecimal,
        val type: ExpenseEntryType
    )

    data class SetupConfig(
        val totalBudget: BigDecimal,
        val currency: AppCurrency,
        val expenses: List<ExpenseEntry>
    )

    fun setup(setupConfig: SetupConfig) {
        val remaining = setupConfig.expenses.sumOf { it.cost }
        val entries = setupConfig.expenses.groupBy { it.type }
            .map { (type, expenses) -> PieEntry(expenses.sumOf { it.cost }.toFloat(), type.name) }
            .plus(PieEntry(remaining.toFloat(), "Remaining"))
            .filter { it.value > 0 }

        val dataset = PieDataSet(entries, "")
        dataset.valueFormatter = PercentFormatter()
        dataset.colors = ColorTemplate.COLORFUL_COLORS.toList()
        dataset.sliceSpace = 5f

        binding.chart.data = PieData(dataset)
        binding.chart.invalidate()
        binding.chart.notifyDataSetChanged()
    }

}
