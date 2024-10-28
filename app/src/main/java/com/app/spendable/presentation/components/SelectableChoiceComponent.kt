package com.app.spendable.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import com.app.spendable.databinding.ComponentSelectableChoiceBinding
import java.io.Serializable

class SelectableChoiceComponent(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    data class Choice(
        val id: String,
        val label: String,
        @DrawableRes val icon: Int
    ) : Serializable

    private var binding: ComponentSelectableChoiceBinding

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ComponentSelectableChoiceBinding.inflate(inflater, this, true)
    }

    fun setup(choice: Choice) {
        binding.icon.setImageResource(choice.icon)
        binding.label.text = choice.label
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        binding.root.isSelected = selected
    }

}
